[![GitHub release](https://img.shields.io/github/release/sismics/play-teedy.svg?style=flat-square)](https://github.com/sismics/play-teedy/releases/latest)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# play-teedy plugin

This plugin adds [Teedy](https://teedy.io/) support to Play! Framework 1 applications.

# Features

# How to use

####  Add the dependency to your `dependencies.yml` file

```
require:
    - teedy -> teedy 1.2.0

repositories:
    - sismicsNexusRaw:
        type: http
        artifact: "https://nexus.sismics.com/repository/sismics/[module]-[revision].zip"
        contains:
            - teedy -> *

```
####  Set configuration parameters

Add the following parameters to **application.conf**:

```
# Teedy configuration
# ~~~~~~~~~~~~~~~~~~~~
teedy.mock=false
teedy.api.url=https://teedy.example.com/api
teedy.authToken=12345678
```
####  Use the API

```
DocumentListResponse response = TeedyClient.get().getDocumentservice().listDocument("tag:Imported");
```

####  Mock the Teedy server in dev

We recommand to mock Teedy in development mode and test profile.

Use the following configuration parameter:

```
teedy.mock=true
```

# License

This software is released under the terms of the Apache License, Version 2.0. See `LICENSE` for more
information or see <https://opensource.org/licenses/Apache-2.0>.
