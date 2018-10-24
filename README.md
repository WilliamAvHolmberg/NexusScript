# NexusScript

Skeleton for creating OSBot Scripts

## Getting Started

These instructions will get you a copy of the project up and running on your local machine, making you ready to create a new script

### Prerequisites

None

### Installing

Download project
Add OSBot to project library

## Functions

This skeleton comes with a few precoded functions suchs as

* NodeHandler
* BankHandler
* GrandExchangeHandler
* GearHandler
* COMING SOON - NexusCommunication - Making it possible to communicate with server thus providing access to database etc.

### NodeHandler

* This is our main controller that takes care of what to do. eg. What node to execute
* Takes a task "as argument"
* E.G WoodcuttingTask
* WoodcuttingTask contains information such as bankArea, actionArea, treeName and axe
* When you create a new Task, such as WoodcuttingTask you are also required to create a controller for that task
#### ->WoodcuttingHandler
In traditional use of NodeSystems your Node Class contains a "shallExecute" boolean. In our case, we let our controller take care of that logic.
In WoodcuttingHandler we check, for instance, if player does not have axe: (Remember, axe is provided from NodeHandler as 'CurrentTask')
```
if player does not have axe :
Give instruction to BankHandler that we need to withdraw a new item(axe) ---- (MORE ABOUT BankHandler further into this text)
if player is not in action Area (&& previous statement is false)
return walkToAreaNode.setArea(currentTask.getArea) --------- we return a walkToAreaNode that is defined in our main controller
setArea returns the whole object.
if player is not animating
return cutNode.setTree(currentTask.getTree())
etc
etc
```
walkToAreaNode and cutNode are created by you.

### BankHandler - Description is coming soon

### GrandExchangeHandler - Description is coming soon

### GearHandler - Description is coming soon

### NexusCommunication - Coming soon

## Author

* **William Holmberg**

