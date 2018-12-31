class Logger:
    """ Keeps logs of the simulation """

    def __init__(self, seed):
        self.seed = seed
        self.logs = []
        self.id_counter = 1

    def new(self, ev_type, is_hq, user, layer, start, end, tx_device, rec_device, freq, prev_ev_id=None):
        """ Creates new log """
        log = Log(self.id_counter, ev_type, "HQ" if is_hq else "SQ", user, layer, start, end, tx_device, rec_device,
                  freq, prev_ev_id)
        self.logs.append(log)
        self.id_counter += 1

        return log.id

    def save(self):
        """ Writes logs to file """
        file = open("log_" + str(self.seed) + ".txt", 'w')
        header = "| {} | {} | {} | {} | {} | {} | {} | {} | {} | {} | {} |".format("EV ID".ljust(5),
                                                                                   "PV ID".ljust(5),
                                                                                   "EV TYPE".ljust(12),
                                                                                   "REQ TYPE".ljust(8),
                                                                                   "USE".ljust(3),
                                                                                   "LAYER".ljust(5),
                                                                                   "START TIME".ljust(12),
                                                                                   "END TIME".ljust(12),
                                                                                   "TX DEVICE".ljust(10),
                                                                                   "REC DEVICE".ljust(10),
                                                                                   "FREQUENCY".ljust(10))
        header += "\n" + ("-" * 126)
        file.write(header + "\n")
        # for log in sorted(Log.logs, key=lambda k: k.start):
        for log in self.logs:
            file.write(str(log) + "\n")
        header += ("-" * 126)


class Log:
    """ Represents log record for calculations """

    def __init__(self, id, ev_type, req_type, user, layer, start, end, tx_device, rec_device, freq, prev_ev_id=None):
        self.id = id
        self.ev_type = ev_type
        self.req_type = req_type
        self.user = user
        self.layer = layer
        self.start = start
        self.end = end
        self.tx_device = tx_device
        self.rec_device = rec_device
        self.freq = freq
        self.prev_ev_id = prev_ev_id if prev_ev_id else self.id

    def __str__(self):
        return "| {} | {} | {} | {} | {} | {} | {} | {} | {} | {} | {} |".format(str(self.id).ljust(5),
                                                                                 str(self.prev_ev_id).ljust(5),
                                                                                 self.ev_type.ljust(12),
                                                                                 self.req_type.ljust(8),
                                                                                 self.user.ljust(3),
                                                                                 self.layer.ljust(5),
                                                                                 "{:.2f} sec".format(self.start).ljust(
                                                                                     12),
                                                                                 "{:.2f} sec".format(self.end).ljust(
                                                                                     12),
                                                                                 (
                                                                                     "NON" if self.tx_device is None else "dev_{}".format(
                                                                                         self.tx_device)).ljust(10),
                                                                                 (
                                                                                     "NON" if self.rec_device is None else "dev_{}".format(
                                                                                         self.rec_device)).ljust(10),
                                                                                 (
                                                                                     "NON" if self.freq is None else "f_{}".format(
                                                                                         self.freq + 1)).ljust(10))
