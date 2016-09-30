# SEG2105: Lab 2 [![build status: unknown](https://travis-ci.org/SEGBK/lab2.svg?branch=master)](https://travis-ci.org/SEGBK/lab2)

Networking-oriented TicTacToe game for SEG2105: Lab 2.

*All source code exists between [Game.java](Game.java) and the [`lib/`](lib) directory.*
*Full reference available at [segbk.github.io/lab2](http://segbk.github.io/lab2).*

## Building

A [Makefile](Makefile) is provided for all build steps. The following is a short description:

 - **all**: compiles all source code.
 - **run**: runs the application.
 - **test**: runs unit tests.
 - **docs**: re-compiles the docs.

## Usage

When you start the application, you can enter a connection string or a port number. If you
enter a port number, the application will assume the role of a TCP server. If you entire a connection string,
the application proceeds accordingly.

The connection string is a standard URI as defined by [RFC 3986](https://tools.ietf.org/html/rfc3986).

## Connection String

The connection string is essentially just: [protocol]://[hostname/ip]:[port]/.

The final slash is optional but the rest is mandatory. The port number must be specified
as well as it is required for this application.