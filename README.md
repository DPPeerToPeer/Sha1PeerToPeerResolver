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

## App logic activity diagram:

![app_logic_diagram](DP_Activity_Diagram.png)

## TCP Sequence Diagram
![TCPseqDiagram](TCPseqDiagram.png)

## Deployment Diagram (local network)
![TCPseqDiagram](DeploymentDiagramLocal.png)

## Deployment Diagram (global)
![TCPseqDiagram](DeploymentDiagramGlobal.png)

## 3 Design Patterns used

### Proxy
We decated to use proxy pattern to make separate class which is responsible for logging calls of methods in NodesRepository. Before we put logging logic directly in NodesRepository so it was quiet messy and we decaded to move that logic somewhere

![proxy_diagram](nodes/proxy_pattern_diagram.png)

### Factory
We needed to create instances of SingleNodeConnectionHandler on demand directly in SingleNodeConnectionRepository methods. If we used constructor of SingleNodeConnectionHandler directly, it would be harder to make tests of SingleNodeConnectionHandler isolated. By providing factory for creating such classes it's possible to mock newly created instances and verify creation of this objects.  

![factory_diagram](network/factory_pattern_diagram.png)

### Facade
The basic functionality that have to be implemented is a connection between different computers.
The connection is based on a TCP/IP stack sockets from external library. The problem is that a lot of
objects have to be initialised, it is a need for tracking dependencies and calling the methods in the right order.
The solution is implementing the facade. It allows an user to easily utilize the most important functions by calling
SocketsFacadeModules' methods.
![facade_diagram](sockets-facade/sockets_facade_diagram.png)


