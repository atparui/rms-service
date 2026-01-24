package com.atparui.rmsservice.config;

/**
 * Placeholder for a DatabaseClient filter hook. The current Spring R2DBC API
 * available in this project does not expose a builder filter method, and we
 * already log all R2DBC statements (with bindings and connection IDs) via the
 * r2dbc-proxy listener. Keeping this class empty avoids build issues while
 * retaining the package path for potential future hooks if the API changes.
 */
public class MenuDatabaseClientLoggingConfig {}
