# ...  A comment, producing no effect.
I FILE Include.  Replace this statement with the contents of FILE,
      interpreted relative to the directory containing the .in file.
C DIR  Create, if necessary, and switch to a subdirectory named DIR under
      the main directory for this test.  If DIR is missing, changes
      back to the default directory.  This command is principally
      intended to let you set up remote repositories.
T N    Set the timeout for gitlet commands in the rest of this test to N
      seconds.
+ NAME F
      Copy the contents of src/F into a file named NAME.
- NAME
      Delete the file named NAME.
> COMMAND OPERANDS
LINE1
LINE2
...
<<<
      Run gitlet.Main with COMMAND ARGUMENTS as its parameters.  Compare
      its output with LINE1, LINE2, etc., reporting an error if there is
      "sufficient" discrepency.  The <<< delimiter may be followed by
      an asterisk (*), in which case, the preceding lines are treated as
      Python regular expressions and matched accordingly. The directory
      or JAR file containing the gitlet.Main program is assumed to be
      in directory DIR specifed by --progdir (default is ..).
= NAME F
      Check that the file named NAME is identical to src/F, and report an
      error if not.
* NAME
      Check that the file NAME does not exist, and report an error if it
      does.
E NAME
      Check that file or directory NAME exists, and report an error if it
      does not.
D VAR "VALUE"
      Defines the variable VAR to have the literal value VALUE.  VALUE is
      taken to be a raw Python string (as in r"VALUE").  Substitutions are
      first applied to VALUE.