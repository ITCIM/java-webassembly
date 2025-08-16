# TeaVM WebAssembly GC – Interactive Demo (Java + Maven)

This project compiles Java to **WebAssembly (GC backend)** using **TeaVM** and provides an **interactive HTML** page to invoke Java functions from the browser.

## Features
- Primitive ops: `add(a,b)`, `isGreater(a,b)`
- String ops: `concat(a,b)`, `substring(s,begin,end)`
- List ops (CSV-based): `sortListCSV(csv)`, `listGetAt(csv,index)`
- Set ops (CSV-based): `sortSetCSV(csv)`, `setContains(csv,value)`
- In-memory Map cache: `cachePut(key,value)` (returns previous value or empty string), `cacheGet(key)`

## Build
```bash
mvn clean package
```
Artifacts will be under `target/dist/`:
```
target/dist/
  index.html
  wasm-gc/
    app.wasm
    app.wasm-runtime.js   # TeaVM GC runtime
```

## Run (static server)
Use a static server (browsers block `.wasm` from `file://`). For example:
```bash
cd target/dist
python -m http.server 8000
# then open http://localhost:8000
```

> If your server does not return the `application/wasm` MIME type, the page includes a small fallback that loads via `arrayBuffer`.

## Browser support
WebAssembly GC requires a modern browser (Chrome/Firefox current). Safari may require enabling **Develop → Experimental Features → WebAssembly GC**.

## Notes
- The `main(String[])` method is intentionally empty; everything is exported via `@Export` and wired in the interactive HTML.
- CSV is used to keep list/set interop simple. You can switch to JSON if desired.


## Runtime file name
This project **always** expects the GC runtime at:
```
target/dist/wasm-gc/teavm-wasm-gc.js
```
The Maven goal `copy-webassembly-gc-runtime` copies that exact file into `wasm-gc/`. The WebAssembly module is:
```
target/dist/wasm-gc/app.wasm
```
And `index.html` loads them at those exact paths.


## Expected files under target/dist/wasm-gc
This project assumes TeaVM 0.12.x produces:
- `classes.wasm-runtime.js` (GC runtime loader)
- `app.wasm` (your module, as configured via `targetFileName=app`)

`index.html` loads exactly these two fixed paths:
```html
<script src="wasm-gc/classes.wasm-runtime.js"></script>
...
await TeaVM.wasmGC.load("wasm-gc/app.wasm");
```
