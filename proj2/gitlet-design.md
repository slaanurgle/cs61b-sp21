# Gitlet Design Document

**Name**: Slaanurgle

## Classes and Data Structures

Commits are represented by class ` Commit`. Blobs are represented by class `Repository`. Blobs are `file` Object.

use a TreeMap to implement the blobs.

use nodes to implement commits.

### Class 1 Commit

Commit represent a node that contains **commit info** and **references to the blobs that the commit involves.**

In terms of the data structure to store commits, I use a **Graph**.  The Graph is a **Acyclic Directed Graph**. I use **adjacency lists** to implement the graph. In the graph, the direction should be from   child commit to parent commit, because the checkout command will traverse to parents.

**Commit IDs:** The commit ids will be use to search the specified commit (through the filename), it serves as keys in the Graph. 

#### Fields

1. Field 1
2. Field 2

### Class 2 Repository

Class Repository holds all the files in the directory `.gitlet`.

I use TreeMap to implement the blobs.

**IDs**: The blobs are different version of the files. The file ids will be use to search the specified file, it serves as keys in the TreeMap.

#### Fields

1. Field 1
2. Field 2

### (Test) Class 3 Branch

Branches are pointers pointing to the commits.


## Algorithms

## Persistence

The `.gitlet` Folder has these subdirectories: `added`, `removed`, `commits`, `files`. 

 In the `commits` folder are folders storing each commit, named according to **the first 6 digits of the hexadecimal expression of the commit id**. In the files folder are the folders storing each blob, named according to **the first 6 digits of the hexadecimal expression of the file id**.

In the `added` and `removed` folders are the added files and removed files(use `gitlet rm` command), the file names are **their original name**, because in these folder, there are only one version and we do not need to check the identicality.

```
CWD                             <==== Whatever the current working directory 
.gitlet                     <==== All persistant data is stored within here
├── added                   <====
    ├── a
    ├── ...
├── removed 
    ├── c
    ├── ...
├── commits   
    ├── 45e9f8
    ├── ...
└── files                      <==== 
    ├── 3a243e                <==== 
    ├── d741a3
    ├── ...
```

The `Commit` will set up persistence. It will:

1. Contain all the Commit info
2. Contain the branches, **master should be maintain**

The `Repository` will set up persistence. It will: 

1. Create all the files and directories needed. **All file operation are done in this class.**
2. Create **HEAD pointer**, we do not need to create HEAD after initializing.
