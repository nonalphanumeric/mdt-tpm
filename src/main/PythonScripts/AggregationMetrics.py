import pandas as pd
import matplotlib.pyplot as plt

import pandas as pd
import matplotlib.pyplot as plt

# Read the CSV file into a DataFrame
df = pd.read_csv('/home/work/Desktop/JBotEvaluation/AggregateTopology.csv', delimiter=';')

# Group the data by clock cycle and find the largest subgraph for each cycle
biggest_connected = df.groupby('clock')['size_of_subgraph'].max().reset_index()

# Calculate the ratio of the largest subgraph to the total number of nodes
total_nodes = df['total_number_of_nodes'].iloc[0]
biggest_connected['ratio'] = biggest_connected['size_of_subgraph'] / total_nodes

# Plot the ratio over time
plt.plot(biggest_connected['clock'], biggest_connected['ratio'])
plt.xlabel('Clock')
plt.ylabel('Ratio')
plt.show()
