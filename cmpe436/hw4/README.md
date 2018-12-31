Mustafa Enes Çakır
2013400105
enes@cakir.web.tr
CmpE436 Assignment 4

Source code of project is "src" folder.

# Problem 6.1
    Base code comes from book. I improved main method of NameServer class for fault-tolerant.
    Name server is chosen randomly.
    handleClient() method throws errors randomly.
    If the server is down, other name server handle client and broken name server is restarted with current name table.
    Entry point of server is main method of NameServer class.
    To start server, nameServer constant in Problem61.Symbols class have to be your local IP.

    You can use NameClient class for sending requests.
    Example usage:
        `java NameClient insert [NAME] [HOSTNAME] [PORT]`
        `java NameClient search [NAME]`

    Terminal usage:
        `java -classpath . Problem61.NameClient insert google google.com 1000`

# Problem 6.3
    Connector, IntLinkedList, Linker, Msg and Topology classes are from book.
    I developed SynchronousLinker that is extended from Linker class.
    I override receiveMsg and sendMsg methods.
    SynchronousLinker has an BinarySemaphore.
    receiveMsg method sends message that has `done` tag to sender when receiving is done.
    When sendMsg get done message, it unblocked.
