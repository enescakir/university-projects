def change_style(str, style):
    if style == "green":
        return "\033[92m{}\033[00m".format(str)
    elif style == "blue":
        return "\033[36m{}\033[00m".format(str)
    elif style == "info":
        return "\033[92m \033[01m{}\033[00m".format(str)
    elif style == "user":
        return "\033[97m\033[104m{}\033[00m".format(str)
    elif style == "bold":
        return "\033[01m{}\033[00m".format(str)
    elif style == "time":
        return "\033[01m\033[97m\033[101m{}\033[00m".format(str)
    elif style == "red":
        return "\033[31m{}\033[00m".format(str)
    return str
