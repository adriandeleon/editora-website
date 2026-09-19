---
title: "Editora 0.18.4: JavaFX 27 and one-step JUnit debugging"
description: "Editora 0.18.4 moves to JavaFX 27, adds debugging for the JUnit test at the caret, strengthens Local History, and makes the supported build toolchain explicit."
date: 2026-09-15T21:51:59-06:00
version: "0.18.4"
---

**Editora 0.18.4** is out. This release updates the desktop runtime, shortens
the path from a failing JUnit test to its debugger, and reinforces Local History
and Java 27 compatibility. Download it from the
[0.18.4 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.18.4).

## Debug the test under the caret

Java developers can now start a debugging session for the JUnit test method at
the caret directly from the editor context menu or command palette. Editora
launches the test through Maven or Gradle and automatically attaches to the
suspended test JVM, removing the need to build a temporary run configuration.

## A current desktop runtime

The UI runtime moves from JavaFX 26.0.2 to JavaFX 27. The JDK 25 release
baseline already meets its requirements, and JavaFX's extended-window API is
now stable, so Editora no longer needs the preview flag for that integration.

Java 27 compatibility is also a blocking CI lane. Java 25 remains the release
baseline, while upgraded coverage tooling and a dedicated formatting lane keep
both JDK generations tested without depending on formatter support that has not
caught up yet.

## Safer history and clearer builds

Local History now protects the body of an in-flight revision until its index
entry has been published, closing a race with garbage collection. Streamed AI
responses on Java 27 retain bounded connection and idle-read waits without
placing an absolute deadline on a healthy long response.

The Maven build plugins and compatible libraries have been refreshed, and
Maven Enforcer now checks the documented Maven 3.9 and JDK 25 requirements.
Personal Notes also use the same compact source-line treatment as Structure and
Bookmarks.

The complete list is on the [What's New](/whats-new) page.
