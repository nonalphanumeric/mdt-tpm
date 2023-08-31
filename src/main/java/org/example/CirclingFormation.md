# Circling formation

## Parameters:
* `radius` - radius of the circle
* `center` - center of the circle

This document aims to explain the formation of a circle of drones.

The formation is staged, meaning that multiple phases are required to reach the final formation.

## Phase 1: Initialisation

The first phase is the initialisation phase. In this phase,
a drone, called the **Brain Of Operation** (BOP) launch the initialisation process by sending a broadcast `CirclingFormationInit` message using the 
**one to many to one** communication pattern, the same used to initialise the spanning tree (see SpanningTreeNode).
This broadcast aims to inform all the drones that the initialisation process has started. 

Each drones will propagate the message to its neighbours, 
except the one that sent the message. The drone then waits to receive a BACK message from
all of its children. This BACK message contains the identity of all the children of the drone.
When the drone receives a BACK message from all its children, it sends a BACK message to its parent.
Alongside the identity of its children, the drone also sends its own identity and position.

Thus, at the end of this initialisation phase, the BOP will have received data about all the drones: Their identity,
their position and the identity of their children.

## Phase 2: Computing

The second phase is the formation phase. In this phase, the BOP will compute the position of each drone.
Firstly, it will compute N positions, where N is the number of drones that took part in the initialisation phase.
Those positions are computer using the following formula: `position = center + radius * unit_vector(k * (2PI/N))`.
The `k` variable is the index of the drone in the list of drones that took part in the initialisation phase.

Now the BOP will have a list of candidate positions and the list of drones alongside their position.
The BOP will compute the distance between each drone and its candidate position. 
The BOP construct a dictionary that maps a drone to its candidate position by selecting the candidate position that is the closest to the drone.

## Phase 3: Sending

The third phase is the sending phase. In this phase, the BOP will send the position of each drone to its children using the
same **one to many to one** communication pattern. The BOP will send a `CirclingFormation` message to each of its children alongside
the dictionary that maps drones to candidate positions. The drone will then propagate the message to its children, except the one that sent the message.

The same as in the initialization phase, a BACK message is sent to the parent when the drone receives a `CirclingFormation` message from all its children.
When the BOP receives a `CirclingFormation` message from all its children, it sends back a `CirclingFormationDone` message to its children
that will be propagated.

## Phase 4: Moving

The fourth phase is the moving phase. In this phase, the drones will move to their new position.

