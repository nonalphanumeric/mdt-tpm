
import pandas as pd
from scipy.spatial import distance
import numpy as np
import matplotlib.pyplot as plt

# Read in the CSV file
df = pd.read_csv('/home/work/Desktop/JBotEvaluation/AggregateTopology-2023-02-27_18-41-14.csv', sep=';')

# Define the bounds of the study area
xmin, xmax = 0, 600
ymin, ymax = 0, 400

# Define the time points to measure
times = df['clock'].unique()

# Initialize a list to store the average mdn values
avg_mdns = []

# Loop over the time points and calculate the average mdn for each
for time in times:
    # Extract the positions of the nodes at this time point
    nodes = df.loc[df['clock'] == time, 'position'].str.split(':', expand=True)
    nodes.columns = ['x', 'y']
    nodes = nodes.astype(float).values
    
    # Calculate the distances between all pairs of nodes
    distances = distance.squareform(distance.pdist(nodes))
    
    # Calculate the observed mdn values for each node
    mdns = np.zeros(len(nodes))
    for i in range(len(nodes)):
        mdns[i] = np.mean(np.sort(distances[i])[1:])
    
    # Calculate the expected mdn values for each node under random distribution
    n = len(nodes)
    expected_mdns = np.zeros(n)
    expected_density = n / ((xmax-xmin)*(ymax-ymin))
    for i in range(n):
        area = np.pi * mdns[i]**2
        expected_mdns[i] = np.sqrt((area/expected_density)/np.pi)
    
    # Normalize the observed mdn values
    normalized_mdns = mdns / expected_mdns - 1
    
    # Calculate the average mdn for all nodes
    avg_mdn = np.mean(mdns)
    avg_mdns.append(avg_mdn)

   

# Plot the average mdn values over time
fig, ax = plt.subplots()
ax.plot(times, avg_mdns)
ax.set_xlabel('Time')
ax.set_ylabel('Average MDN')
ax.set_title('Average MDN over Time')
plt.show()