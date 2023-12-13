# Sha1PeerToPeerResolver

## Modules:
![dependency diagram](dependency_diagram_v2.png)

#### :network
Responsible for communication between nodes and holding connections  

Module contract:  

![dependency diagram](network/network_contract_diagram.png)

Internals:  

![dependency diagram](network/network_internal_diagram.png)

#### :sockets-facade
Facade module which has all sockets entities. Currently it depends on ktor non-blocking sockets

![dependency diagram](sockets-facade/sockets_facade_diagram.png)

#### :common
Classes shared between many modules
#### :nodes
Responsible for holding information about nodes and wrappers for sending broadcas information for all nodes
#### :calculation
Responsible for holding information about batches and their states, making calculations
#### Sha1PeerToPeerResolver (main module)
Jetpack Compose Desktop UI, joins all other modules, gives controle over calculation via UI

