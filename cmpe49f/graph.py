import matplotlib.pyplot as plt
import matplotlib.patches as mpatch


def draw_plot(logs, sim_time):
    fig, ax = plt.subplots()
    data = []
    for i in range(10):
        data.append({"x": [], "color": [], "border": []})

    for log in logs:
        if log.ev_type == "SERVED" and log.user == "PU":
            data[log.freq]['x'].append((log.start, log.end - log.start))
            data[log.freq]['color'].append('#7db1fc')
            data[log.freq]['border'].append('#004c99')
        elif log.ev_type == "SERVED" and log.user == "SU" and log.layer == "BASE":
            data[log.freq]['x'].append((log.start, log.end - log.start))
            data[log.freq]['color'].append('#a8e2a5')
            data[log.freq]['border'].append('#24a624')

        elif log.ev_type == "SERVED" and log.user == "SU" and log.layer == "ENH":
            data[log.freq]['x'].append((log.start, log.end - log.start))
            data[log.freq]['color'].append('#f99f9f')
            data[log.freq]['border'].append('#bc48aa')

        elif log.ev_type == "DROPPED" and log.user == "SU" and log.layer == "BASE":
            data[log.freq]['x'].append((log.start, log.end - log.start))
            data[log.freq]['color'].append('#fcd1a4')
            data[log.freq]['border'].append('#e1b43f')

        elif log.ev_type == "DROPPED" and log.user == "SU" and log.layer == "ENH":
            data[log.freq]['x'].append((log.start, log.end - log.start))
            data[log.freq]['color'].append('#f86161')
            data[log.freq]['border'].append('#c50c0c')

    for i, channel in enumerate(data):
        ax.broken_barh(channel['x'], (700 + i * 2, 2), facecolors=channel['color'], edgecolors=channel['border'])

    ax.set_ylim(700, 720)
    ax.set_xlim(0, sim_time)
    ax.set_xlabel('Channel history')
    ax.set_yticks([702 + 2 * i for i in range(10)])
    ax.set_yticklabels(["f{}".format(i + 1) for i in range(10)])
    ax.grid(True)
    ax.legend([mpatch.Rectangle((0, 0), 1, 1, fc="#7db1fc"),
               mpatch.Rectangle((0, 0), 1, 1, fc="#a8e2a5"),
               mpatch.Rectangle((0, 0), 1, 1, fc="#f99f9f"),
               mpatch.Rectangle((0, 0), 1, 1, fc="#fcd1a4"),
               mpatch.Rectangle((0, 0), 1, 1, fc="#f86161")],
              ['PU', 'SU-BASE', 'SU-ENH', 'SU-BASE Drop', 'SU-ENH Drop'])
    plt.show()
