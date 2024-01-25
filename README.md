# Sha1PeerToPeerResolver

## Modules:
![dependency diagram](dependency_diagram_v2.png)

#### :network
Responsible for communication between nodes and holding connections  

Module contract:  

![class diagram](network/network_contract_diagram.png)

Internals:  

![class diagram](network/network_internal_diagram.png)

#### :sockets-facade
Facade module which has all sockets entities. Currently, it depends on ktor non-blocking sockets

![class diagram](sockets-facade/sockets_facade_diagram.png)

#### :common
Classes shared between many modules

![class diagram](common/common_diagram.png)


#### :nodes
Responsible for holding information about nodes and wrappers for sending broadcast information for all nodes

![class diagram](nodes/nodes_diagram.png)


#### :calculation
Responsible for holding information about batches and their states, making calculations

![class diagram](calculation/calculation_diagram.png)


#### Sha1PeerToPeerResolver (main module)
Jetpack Compose Desktop UI, joins all other modules, gives control over calculation via UI

![class diagram](src/main_diagram.png)

## App logic activity diagram:

![app_logic_diagram](DP_Activity_Diagram.png)

## TCP Sequence Diagram
![TCPSeqDiagram](TCPseqDiagram.png)

## UDP Sequence Diagram
![UDP_sequence_diagram.png](UDP_sequence_diagram.png)

## Deployment Diagram (local network)
![DeploymentDiagram](DeploymentDiagramLocal.png)

## Deployment Diagram (global)
![DeploymentDiagram](DeploymentDiagramGlobal.png)

## Division into Batches Activity Diagram
![class diagram](calculation/activityBatchDiagram.png)

## 3 Design Patterns used

### Decorator
We decided to use decorator pattern to make separate class which is responsible for logging calls of methods in NodesRepository. Before we put logging logic directly in NodesRepository, so it was quite messy, and we decided to move that logic somewhere

![decorator_diagram](nodes/decorator_pattern_diagram.png)

### Simple Factory
We needed to create instances of SingleNodeConnectionHandler on demand directly in SingleNodeConnectionRepository methods. If we used constructor of SingleNodeConnectionHandler directly, it would be harder to make tests of SingleNodeConnectionHandler isolated. By providing factory for creating such classes it's possible to mock newly created instances and verify creation of these objects.  

![factory_diagram](network/factory_pattern_diagram.png)

### Facade
The basic functionality that have to be implemented is a connection between different computers.
The connection is based on a TCP/IP stack sockets from external library. The problem is that a lot of
objects have to be initialised, it is a need for tracking dependencies and calling the methods in the right order.
The solution is implementing the facade. It allows user to easily utilize the most important functions by calling
SocketsFacadeModules' methods.
![facade_diagram](sockets-facade/facade_diagram.png)


