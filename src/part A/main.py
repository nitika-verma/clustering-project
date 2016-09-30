import matplotlib.mlab as mlab
import matplotlib.pyplot as plt
from scipy.stats import norm
from commands import getoutput

def helper(fin,n,xlabel,ylabel,title):
  line = fin.readline()
  data = line.split()
  data = [float(i) for i in data]
  line = fin.readline()
  value = float(line)
  line = fin.readline()
  devi = float(line)
  devi=round(devi,5)
  plt.subplot(n, axisbg='blue')
  plt.axvline(x=value,color='red')
  percentage = 100/(2*devi*devi)
  percentage=round(percentage,4)
  perc=str(percentage)+'%'
  if(devi>0):
    plt.text(value,15,value,color='red',horizontalalignment='right')
    plt.text(value,12,perc,color='red',horizontalalignment='right')
  else:
    plt.text(value,15,value,color='red',horizontalalignment='left')
    plt.text(value,12,perc,color='red',horizontalalignment='left')

  plt.hist(data, bins=10, color='y')
  (mu, sigma) = norm.fit(data)
  y = mlab.normpdf( 10, mu, sigma)
  #l = plt.plot(10, y, 'r--', linewidth=1)
  plt.xlabel(xlabel)
  plt.ylabel(ylabel)
  plt.title(title)


if __name__ == '__main__':
  fin = open("stats.data",'r')
  helper(fin,321,'Edge Ratio','Frequency','Distribution of ratio of same linking edges to all edges')
  helper(fin,324,'Clustering Coefficient','Frequency','Distribution of CC considering all edges')
  helper(fin,325,'Clustering Coefficient','Frequency','Distribution of CC considering same linking edges')


  plt.show()
  fin.close()
  plt.close()
  getoutput('java -jar analyser.jar')

