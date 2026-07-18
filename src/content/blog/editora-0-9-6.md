---
title: "Editora 0.9.6: what a per-feature audit turns up"
description: "0.9.6 came out of walking through the editor one feature at a time and asking what could go wrong. Here's what that found, and why the boring fixes are the ones that matter."
date: 2026-07-17
author: Adrian De Leon
tags: [release]
---

Most of 0.9.6 came from a single exercise: taking one feature at a time, reading
it as if I'd never written it, and asking what happens on the inputs nobody
tests with. Not the happy path, the other ones. A file reached through a symlink.
A path with a space in it. A folder named with a shell character. A note whose
text appears three thousand times in the file.

It turned out to be a lot. The changelog for this release has over a hundred
entries, and almost none of them are features. That's the point of the release,
so it's worth saying why that kind of work is worth doing.

## The credential that crossed the network

The one that bothered me most: if you pointed an AI provider at a plain
`http://` address on another machine and had an API key configured, Editora
attached the key to the request. Every idle pause while typing, inline
completion fired, and the key went out over the wire unencrypted. Nothing on
screen said so.

Worse, the key was a single shared field. Configure Anthropic, then switch to a
local or hosted OpenAI-compatible provider, and your Anthropic key went to that
provider's endpoint, which had no business seeing it. On most developer machines
`ANTHROPIC_API_KEY` is exported, and Editora fell back to it for *any* provider,
so selecting a local model that needs no key at all still sent your Anthropic
credential to whatever was configured.

All three are fixed. Keys are stored per provider now, the environment fallback
only applies to the provider it belongs to, and Editora refuses to attach a key
to a non-loopback `http://` host before it connects. Local inference on
`127.0.0.1` is untouched, because that never leaves your machine and that's the
whole point of running a model locally.

## The server you thought you were talking to

SFTP accepted any host key. That means anyone positioned between you and your
server, hostile Wi-Fi, a compromised router, a DNS answer that isn't real, could
impersonate it, and with password auth Editora would simply hand them the
password, then the contents of every file you opened or saved.

Now Editora checks the key against `~/.ssh/known_hosts`, the same file `ssh`
uses. A host you've accepted at the terminal connects with no prompt. A new host
shows its fingerprint and asks. A host whose key has *changed* is refused, with
no button to wave it through, because that's exactly what an impersonation looks
like and a friendly "accept anyway" is how these attacks succeed.

## The folder name that ran code

On Windows, "Open Terminal Here" built a command line with the folder path
spliced into it (`cmd /k cd /d <dir>`). Windows folder names can legally contain
`&`, `|`, and `^`. So a repository that shipped a directory named with a shell
metacharacter ran arbitrary commands the moment you opened a terminal in it, just
from cloning and browsing. The fix is to hand the folder to the child process as
its working directory, where no shell parses it, instead of putting it in a
command line at all.

## The quiet ones

Those are the dramatic examples. Most of the audit was quieter, and honestly more
representative of what the exercise turns up:

- A **language server that crashed stayed cached and looked healthy.** Completion,
  hover, and go-to-definition all silently did nothing while the status bar still
  named the server. A dead server is now detected and dropped.
- A **PlantUML diagram with a name** (`@startuml myclassdiagram`) writes its output
  to `myclassdiagram.png`, but Editora looked for a fixed filename, so it showed a
  bare "render failed" and its export reported success while writing no file.
- An **external tool's output landed in whichever tab was active when it finished**,
  not the one it ran on, so switching tabs during a format run could overwrite an
  unrelated file with the first file's output.
- The **crontab preview explained cron's day-of-month/day-of-week rule backwards.**
  `0 0 13 * 5` was described as Friday the 13th; it actually runs every Friday and
  every 13th, which is the exact gotcha the preview exists to clarify.
- A **personal note whose text is extremely common** could relocate itself thousands
  of lines away to the wrong occurrence after an edit. It's now marked orphaned,
  which is honest and recoverable, instead of confidently wrong.
- **Reset to Defaults reset 23 of 181 settings.** It was a hand-written list of
  setters that stopped being maintained years of features ago, so it silently left
  about 87% of your preferences in place, including the AI API key. It's now driven
  off the same properties the settings file is written from, so a new setting is
  covered the day it's added.

None of those would show up in a demo. All of them would ruin someone's afternoon.

## The few things you'll see

Three changes are visible rather than invisible. The five build tools now stream
into **one tabbed Build Output window** instead of a separate console each, so a
Maven and an npm build running at once land in their own tabs. **Edit Breakpoint**
became a real form, which means logpoints and disabled breakpoints, both fully
built and persisted but with no way to actually create them, can finally be set.
And **Format Document** works for JSON, CSS, and HTML now, whose servers implement
formatting but only advertise it when asked a specific way Editora wasn't asking.

## Why ship a release of fixes

Because a text editor's job is to be trustworthy with your files, your
credentials, and your machine, and none of that is visible until it fails. A
feature you can see; a leaked key or a lost setting you find out about later, at a
bad time. This release is the unglamorous half of building a tool people leave
open all day.

Get it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). The
complete list, all hundred-plus of them, is on the [What's New](/whats-new) page.
