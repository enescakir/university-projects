"""
    Student Name: Mustafa Enes ÇAKIR
    Student Number: 2013400105
    Project Number: 2
    Python Version: 3.6.1
"""


import os, argparse, hashlib, subprocess, re

##############################
########## METHODS ###########
##############################

def parse_arguments():
    """
    Parses arguments from command line.

    ...

    Returns
    -------
    list
        Parsed argument list
    """
    parser = argparse.ArgumentParser(description='Short sample app')
    parser.add_argument('-c', dest="command", action='store')
    parser.add_argument('-p', dest="print", action='store_true', default=False)
    parser.add_argument('-f', dest="file", action='store_true', default=False)
    parser.add_argument('-d', dest="directory", action='store_true', default=False)
    parser.add_argument('extras', action='store', nargs='*')
    return parser.parse_args()

def handle_extra_arguments():
    """
    Extracts regex and directories from arguments

    ...

    Returns
    -------
    regex : str
        Pattern for searching file and directory names. Defaultly it's `.*?`, so it matches with all strings.
    dirlist : list
        Absolute paths list for traversing. Defaultly it's current directory.
    """
    regex = ".*?"
    dirlist = []
    global arguments
    if arguments.extras:
        # If it starts with quote mark, it's a pattern
        if arguments.extras[0][0] == "\"":
            # Remove quotes
            regex = arguments.extras[0][1:-1]
            del arguments.extras[0]

        for path in arguments.extras:
            # If it starts with /, it's absolute path
            if path[0] == "/":
                dirlist.append(path)
            # Add current directory path to relative path
            else:
                dirlist.append(os.path.abspath(path))
    # If no path is given, add current directory path
    if not dirlist:
        dirlist = [os.getcwd()]

    return regex, dirlist

def hash_all():
    """
    Calculates SHA256 hash of files and directories in given paths.

    ...

    Returns
    -------
    dictionary
        Paths associated with hash {path:hash}
    """
    hash_of_paths = {}
    while dirlist:
        path = dirlist.pop()
        ## If dictionary argument given and folder name matches with regex; calculates hash of the directory
        if arguments.directory and re.search(regex, path[path.rfind("/")+1:]):
            ## Don't recalculate it
            if not hash_of_paths.get(path):
                hash_of_paths[path] = hash_dir(path)
        curlist = os.listdir(path)
        for fdname in curlist:
            newpath = path + "/" + fdname
            if os.path.isdir(newpath):
                dirlist.append(newpath)
            else:
                ## If dictionary argument not given or file argument given and file name matches with regex; calculates hash of the file
                if (not arguments.directory or arguments.file) and re.search(regex, fdname):
                    ## Don't recalculate it
                    if not hash_of_paths.get(newpath):
                        hash_of_paths[newpath] = hash_file(newpath)
    return hash_of_paths

def hash_file(path):
    """
    Calculates SHA256 hash of a file.

    ...

    Parameters
    ----------
    path : str
        Absolute path of the file

    Returns
    -------
    str
        Hash value of the file
    """
    cmd = 'shasum -a 256 ' + replace_escapes(path)
    output = subprocess.check_output(cmd,shell=True)
    # `shasum` command adds path to end of hash. It slices string from space.
    # Decodes to `utf-8` for removing literal `b` that represents binary
    return output[:str(output).find(" ")-2].decode("utf-8")

def hash_dir(path):
    """
    Calculates SHA256 hash of a directory. Recursily enters directories.

    ...

    Parameters
    ----------
    path : str
        Absolute path of the directory

    Returns
    -------
    str
        Hash value of the directory
    """
    curlist = os.listdir(path)
    hashes = []
    for name in curlist:
        newpath = path + "/" + name
        if os.path.isdir(newpath):
            hashes.append(hash_dir(newpath))
        else:
            hashes.append(hash_file(newpath))
    # If folder is empty, calculate hash of empty string
    if not hashes:
        hash_object = hashlib.sha256("".encode())
        hashes = [hash_object.hexdigest()]

    hashes.sort()
    hashes_string = ""
    # Concatenate all hashes
    for hash in hashes:
        hashes_string += " " + hash
    # Rehash all hashes to one hash
    hash_object = hashlib.sha256(hashes_string.encode())
    return str(hash_object.hexdigest())

def reverse_path_hash_dict():
    """
    Creates path sets for corresponding hashes

    ...

    Returns
    -------
    dictionary
        Hashes associated with path set {hash:(path_set)}
    """
    hash_dict = {}
    for path, hash in hash_of_paths.items():
        # If hash already added to dict, add path to it
        if hash_dict.get(hash):
            hash_dict[hash].add(path)
        # Create a new set with path and match with hash
        else:
            hash_dict.update({hash:set((path,))})
    return hash_dict

def print_paths():
    """
    Prints absolute path of duplicates
    """
    for hash, paths in hash_dict.items():
        # If there are more than one paths that have same hash print them
        if len(paths) > 1:
            for path in paths:
                print(path)
            # Adds new line between duplicate sets
            print("")

def execute_commands():
    """
    Executes given command for every duplicates
    """
    for hash, paths in hash_dict.items():
        if len(paths) > 1:
            for path in paths:
                cmd = arguments.command + " " + replace_escapes(path)
                os.system(cmd)

def replace_escapes(path):
    """
    Replaces space and quotes for path

    ...

    Returns
    -------
    str
        Safe to use path
    """
    path = path.replace(" ", "\\ ")
    path = path.replace("\"", "\\\"")
    return path

##############################
######## MAIN PROGRAM ########
##############################

# Get arguments
arguments = parse_arguments()

# Get regex and directory list for traversing
regex, dirlist = handle_extra_arguments()

# Get hash of all files and directories
hash_of_paths = hash_all()

# Convert {path:hash} dictionary to {hash:(path_set)} dictionary
hash_dict = reverse_path_hash_dict()

# If there is no given command or print argument is given print duplicates
if not arguments.command or arguments.print:
    print_paths()

# If command is given, execute it on duplicates
if arguments.command:
    execute_commands()
