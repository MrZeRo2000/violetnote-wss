# violetnote-wss

REST backend for **VioletNote** encrypted password files (`*.vnf`). It reads and writes the
AES-encrypted VioletNote data format via [`violetnote-core`](../violetnote-core) and exposes it
as JSON over HTTP. The client is [`violetnote-ang`](../violetnote-ang).

Packaged as a **WAR** (`SpringBootServletInitializer`) and deployed into a local Apache Tomcat,
but it also runs standalone via `Application.main`.

| | |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.x |
| Build | Gradle (wrapper, invoked through `gradlew.ps1`) |
| Artifact | `violetnote-wss.war` (prod) / `violetnote-int-wss.war` (int) |
| Version | `build.gradle` → `version` (currently 1.7.16), served by `/app-info` |

## Prerequisites

* JDK 21+. It is not on `PATH`; `gradlew.ps1` locates it itself through
  `common/builder/builder.psm1` (`Find-Java`), so use that wrapper rather than `gradlew.bat`.
* `mavenLocal()` must contain the sibling libraries — they are **not** on Maven Central:
  * `com.romanpulov:violetnote-core:2.1.6`
  * `com.romanpulov:jutils-core:0.5.3`

  Build and `publishToMavenLocal` them from their own repos first, otherwise dependency
  resolution fails.
* For `deployProd` / `deployInt`: a Tomcat unpacked at `../apache-tomcat-<version>`, where the
  version comes from [`common/config/tomcat.properties`](../common/config/tomcat.properties) —
  the single source of truth shared with the PowerShell builder. Override with
  `-Ptomcat_home=<path>`.

## Commands

```powershell
./gradlew.ps1 build                  # compile + test + assemble the WAR
./gradlew.ps1 clean test --info      # run tests
./gradlew.ps1 clean war deployProd   # build and deploy as violetnote-wss.war
./gradlew.ps1 clean war deployInt    # build and deploy as violetnote-int-wss.war
./gradlew.ps1 printProps             # print the resolved Tomcat webapps folder
```

`bootWar` always produces `build/libs/violetnote-wss-build.war`; the `deploy*` tasks copy it into
Tomcat's `webapps` under the target name. Deploying both gives two independent instances
(prod + int) on one Tomcat, which is exactly what the Angular client expects:

* `environment.ts` (prod build) → `http://localhost:8080/violetnote-wss/`
* `environment.development.ts` → `http://localhost:8080/violetnote-int-wss/`

## Runtime configuration

### Data file location (legacy `/passdata` endpoints only)

`PassDataFileManagementService` reads **servlet context init parameters**, not Spring properties:

| Parameter | Meaning |
|---|---|
| `pass-data-file-name` | local path of the `.vnf` file |
| `pass-data-dropbox_file-name` | remote path inside Dropbox |
| `pass-data-downloaded_file-name` | file name for the Dropbox download; stored in `$CATALINA_BASE/temp`, falling back to `java.io.tmpdir` |

Under Tomcat these come from a context descriptor
(`conf/Catalina/localhost/violetnote-wss.xml`) as `<Parameter>` entries. In tests and standalone
runs they are set through `server.servlet.context-parameters.*` — see
[src/test/resources/application.properties](src/test/resources/application.properties).

Note the underscore/hyphen mix in the parameter names (`pass-data-dropbox_file-name`); it is
inconsistent but load-bearing. `pass-data-downloaded_file-name` is the only one that throws when
missing — the other two silently resolve to `null` and surface as `PassDataFileNotFoundException`.

The **v2 endpoints ignore all of this** and take `fileName` from the request body instead.

### Profiles and logging

* default profile → `application.properties`, `logback.xml`, INFO
* `int` profile → `application-int.properties`, `logback-int.xml`, DEBUG/TRACE

Standalone port is `8081`. Spring Boot Actuator is on the classpath, so `/actuator/health` is
available with default exposure.

## API

Every controller is annotated `@CrossOrigin(origins = "http://localhost:4200")` — the Angular dev
server origin is hard-coded. A different client origin needs a code change.

### v2 — file name supplied per request (current API)

`PassData2` format (`/v2/passdata2`) and legacy `PassData` format (`/v2/passdata`) share the same
shape:

| Method | Path | Body | Returns |
|---|---|---|---|
| POST | `/v2/passdata2` | `{fileName, password}` | decrypted `PassData2` |
| POST | `/v2/passdata2/edit` | `{fileName, password, passData}` | re-read data after write |
| POST | `/v2/passdata2/new` | `{fileName, password, passData}` | newly created data |
| POST | `/v2/passdata2/fileinfo` | `{fileName}` | `{name, exists, valid}` |
| POST | `/v2/passdata` … | same | legacy `PassData` model |

* `edit` reads the existing file first (password check) before writing, then reads it back.
* `new` refuses to overwrite: it fails if the file already exists, and seeds one
  "New Category / New System / New User" entry.
* `fileinfo` reports `valid` by actually creating and deleting the file when it does not exist —
  i.e. it is a writability probe with a side effect on the filesystem.

### Legacy — file name from the servlet context

| Method | Path | Notes |
|---|---|---|
| POST | `/passdata` | read the configured file |
| GET | `/passdata/fileinfo` | info about the configured file |
| GET | `/passdata/filename` | debug string: path + existence |
| POST | `/passdata/password` | echoes the password back (debug) |
| POST | `/passdata/dropbox` | download from Dropbox, then read |
| POST | `/passdata/dropbox/download` | download only |
| POST | `/passdata/dropbox/read` | read the already-downloaded file |
| POST | `/passdata/dropbox/checkedread` | download only if not present, then read |

Dropbox requests carry `{authKey, password}`; `authKey` is passed straight through as a
`Bearer` token to `content.dropboxapi.com`. Nothing is cached or refreshed server-side.

### Misc

| Method | Path | Returns |
|---|---|---|
| GET | `/app-info` | `{version}` from `gr.properties`, filtered at build time |
| GET | `/dropbox/auth?code=…` | echoes the OAuth code back as JSON (redirect landing) |

### Error responses

`ExceptionControllerAdvice` returns **HTTP 200 with an error body** for every failure:

```json
{ "status": 404, "message": "..." }
```

The real status lives in the payload's `status` field. Clients must inspect the body, not the
HTTP status. Keep this in mind before "fixing" it — the Angular client depends on the behaviour.

## Write safety

`AbstractPassDataManagementService.savePassData` never writes in place:

1. serialize to a temp file next to the target,
2. roll existing backup copies (`jutils-core` `FileUtils.saveCopies`),
3. rename the temp file over the target.

A failure at any step raises `PassDataFileWriteException` and leaves the previous file intact.

## Layout

```
src/main/java/com/romanpulov/violetnotewss/
  Application.java        WAR + standalone entry point
  config/                 GrProperties (build version), Jackson, logging
  controller/             REST endpoints + @ControllerAdvice
  services/               read/write orchestration, Dropbox, file resolution
  mapper/                 core model <-> DTO
  model/                  DTOs, requests, responses
  aspect/                 ExecutionLoggingAspect (method timing)
src/main/resources/       application*.properties, logback*.xml, gr.properties
src/main/webapp/WEB-INF/  web.xml (display name only)
src/test/resources/testdata/  test1.vnf, test2.vnf, test1.json
```

## Tests

JUnit 5. `BaseApplicationTest` boots the full context on a random port with `TestRestTemplate`;
`BaseControllerMockMvcTest` covers the controllers without a server. Fixtures resolve relative to
`src/test/resources/testdata` (`TestConfiguration.TEST_ROOT_PATH`), so tests must run with the
project root as the working directory.

`DropboxTest` talks to the real Dropbox API and needs a valid token — expect it to fail on a
machine without one.

## Known rough edges

* `Dockerfile` / `docker-compose.yml` are **stale**: they reference `tomcat:8.5` and a `config/tomcat/`
  directory that does not exist in the repo. Deploy via `deployProd` instead, or restore those
  files before using Docker.
* Passwords travel in plaintext request bodies. This is only safe because the service is meant to
  be bound to localhost; do not expose it on a network without TLS and auth.
* `/passdata/password` and `/passdata/filename` are debug endpoints that echo a password and a
  filesystem path. Worth removing if this is ever reachable beyond localhost.

## Conventions

* Bump `version` in `build.gradle` with each meaningful change; it is surfaced through `/app-info`,
  which is how the deployed instance is identified.
* Commit messages follow `<what changed>, version <x.y.z>`.
