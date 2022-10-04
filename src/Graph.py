import matplotlib.pyplot as plt
import re
import os

plt.style.use('seaborn')

path = "/Users/charleslewis/IdeaProjects/KpopStats/data/"

folder = os.fsencode(path)

filenames = []

for file in os.listdir(folder):
    filename = os.fsdecode(file)
    if filename.endswith(".dat"):
        filenames.append(filename)
for file_name in filenames:
    file = open("/Users/charleslewis/IdeaProjects/KpopStats/data/" + file_name, "r")
    data = file.read()
    file.close()
    pattern = re.compile(r'\n+(\d+\.+\d+)\s+(\d+)')
    matches = pattern.finditer(data)
    percent_latin_lyrics = []
    total_youtube_views = []
    for match in matches:
        percent_latin_lyrics.append(float(match.group(1)))
        total_youtube_views.append(float(match.group(2)))
    plt.scatter(percent_latin_lyrics, total_youtube_views)
    plt.title("Correlation between the percentage of Latin Script lyrics and the total number of YouTube views")
    plt.xlabel("% Latin Script Lyrics")
    plt.ylabel("Total YouTube views (millions)")
    plt.show()
