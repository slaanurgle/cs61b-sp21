# Gitlet Design Document

**Name**: Slaanurgle

## Classes and Data Structures

Commits are represented by class ` Commit`. Blobs are represented by class `Repository`. Blobs are `file` Object.

Use a TreeMap to implement the blobs. Blobs id are compute according to the file and its file name.

Commit id are compute according to the `Commit` Object.

### Class 1 Commit

Commit represent a node that contains **commit info** and **references to the blobs that the commit involves.**

In terms of the data structure to store commits, I use a **Graph**.  The Graph is a **Acyclic Directed Graph**. I use **adjacency lists** to implement the graph. In the graph, the direction should be from   child commit to parent commit, because the checkout command will traverse to parents.

**Commit IDs:** The commit ids will be use to search the specified commit (through the filename), it serves as keys in the Graph. 

#### Fields

1. `private String message`
2. `private Date date`
3. `LinkedList<Commit> parents`
4. `TreeMap<String, String> blobs` The TreeMap maps the file name to file id.

### Class 2 Repository

Class Repository holds all the files in the directory `.gitlet`.

File id: the file id are compute using **the sha1 code of `String filename + String contents`**

#### Fields

1. `addFile(String filename)`: add the given file to `.gitlet/added`. For example `a.txt`
   - Check if HEAD has same version of the file. If so, there should be no `a.txt` in staged folders. Else, there should only be one `a.txt` in staged folders.
   - Check if it is staged(removed or added),  make sure there are only one file of the same name in the staged folders.
2. `commit(String message)`: Create a new commit from `.gitlet/added` and `.gitlet/removed`.
   - Create a `Commit newCommit`, add message, current time.
   - Get the HEAD commit, make it the parent of `newCommit`. Get the blobs of its parent, then check the blobs:
     - If there are files of same name, replace them with the files in staged area:
       - Check if the files are one of the versions of blobs, if so, add reference to them, if not, create new blob and add reference.
   - Clear staged areas.
   - Move the HEAD to the `newCommit`, save the newCommit
3. `removeFile(String filename)`: Add the given file to `.gitlet/remove` 
   - Check if HEAD has the file of same name.
     - If so, **remove the file**, add the file to `.gitlet/removed`, do not need to add contents, make sure there are only one file of the same name in the staged folders.
     - If not, check if it is staged(removed or added):
       - If so, make sure there are no file of the same name in the staged folders.
       - If not, throw error.
4. `printLog()`: Print the commits from HEAD to the initial commit. Only traverse the first parent.
   - Traverse from the HEAD commit, print id(compute instantly), date, message
5. `printGlobalLog()`: Print all commits. 
   - Print all commits in `.gitlet/objects/commits`
6. `find(message)`:  Print the commits id that have the given message.
   - Traverse all commits in `.gitlet/objects/commits`, if a commit has that message, print its id.
7. `printStatus()`: Print branches, staged files, removed files, modification not staged for commit.
   - compare the CWD and the blobs of HEAD, find the different files:
     - If `.gitlet/remove` has the file but CWD also has:
       - Print it in Untracked Files
     - If the CWD does not have the file but HEAD has:
       - It is staged to removed: print in Removed Files
       - Not staged: print `a.txt (deleted)` in Modification Not Staged
     - If the CWD has a file but HEAD does not have:
       - Print it in Untracked files
     - If the CWD has the file, but different version to HEAD:
       - the CWD version is staged: print in Staged Files
       - the CWD version is not staged: print in Modifications Not Staged
8.  `merge(String branch)`: Merge HEAD with BRANCH
   - find the (latest) split point. 
     - If BRANCH is split point, do nothing, then print `Given branch is an ancestor of the current branch.`.
   - The cases below may modified untracked file. So untracked files should be checked first.
     - If HEAD is split point, then checkout to BRANCH, print `Current branch fast-forwarded.`
     - If is the other cases:
       1. 


## Algorithms

## Persistence

The `.gitlet` Folder has these subdirectories: `added`, `removed`, `objects`. 

 In the `objects` folder are folders `commits` and `blobs`. `commits` stores each commit, named according to **the first 6 digits of the hexadecimal expression of the object id**. Some of the objects are commits, they stores the serialized Commit objects. Some of the objects are blobs, they stores one version of a file.

In the `added` and `removed` folders are the added files and removed files(use `gitlet rm` command), the file names are **their original name**, because in these folder, there are only one version and we do not need to check the identicality.

```
CWD                             <==== Whatever the current working directory 
.gitlet                     <==== All persistant data is stored within here
├── added                   <====
    ├── a.txt
    ├── ...
├── removed 
    ├── c.txt
    ├── ...
├── objects
	├── commits
        ├── 45e9f8
        ├── ...
    ├── blobs
        ├── 348a174d5...
        ├── ...
└── branches                      <==== 
    ├── HEAD                <==== 
    ├── master				
    ├── ...
```

The `Commit` will set up persistence. It will:

1. Contain all the Commit info
2. Contain the branches, **master should be maintain**

The `Repository` will set up persistence. It will: 

1. Create all the files and directories needed. **All file operation are done in this class.**
2. Create **HEAD pointer**, we do not need to create HEAD after initializing. **Remember to move HEAD and the branch it is pointing to.**
