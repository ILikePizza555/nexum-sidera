CREATE TABLE IF NOT EXISTS "version"(
    "id" INTEGER PRIMARY KEY CHECK (id = 0),
    "major" INTEGER NOT NULL,
    "date_created" DATETIME NOT NULL DEFAULT datetime(),
);

INSERT OR IGNORE INTO "version"(id, major) VALUES (0, 1);

CREATE TABLE IF NOT EXISTS "bookmarks"(
    "id" INTEGER PRIMARY KEY CHECK (id >= 0),
    "name" TEXT NOT NULL,
    "url" TEXT NOT NULL,
    "favicon" BLOB,
    "path" TEXT NOT NULL,
);