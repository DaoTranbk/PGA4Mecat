import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

data = pd.read_csv('test.csv').values

x = data[:, 0]
y = data[:, 1]
print(x)
print(y)
plt.plot(x, y, 'g^')
plt.show()


