## Part A (Politics-related Books)

### Dataset

The dataset used in this visualization contains purchase patterns of politics- related books on Amazon. The books are classified as left leaning, right leaning or neutral in its political stance. Using this visualization we try and find out if people like to read diverse books that touch upon several different affiliations or they rather like to read stuff that possibly resonates with their own viewpoints.

### Visualization

<img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartA_4.png" width="550px">

<img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartA_5.png" width="550px">

 * The graph considers the books as nodes and the edges as people who have read the 2 books that the edge connects
 * The nodes are colour coded. Red nodes are the capitalist aligned books. Blue nodes represent the neutral politics books and green nodes are the hardcore leftist books.
 * The graph contains clusters. The 2 opposite clusters representing the 2 opposing political views namely the capitalists and the democrats.
 
### Clustering

Each node is considered as a physical mass. The edges between them are regarded as springs. The two masses repel each other.
The clustering happens because the nodes connected by spring tend to come close together due to natural spring forces. Given the data, we noticed that the data is connected in such a manner that this will collect together, the nodes with the same affiliations. Thus creating two visibly different clusters in the visualization.

**Clustering Coefficient** is a measure of degree to which nodes in a graph tend to cluster together. In most networks, and in particular social networks, nodes tend to create tightly knit groups characterised by a relatively high density of ties.

### Statistical Analysis

During statistical analysis we create 100 random graphs. We use the same node definitions but pick (number of edges) pairs of nodes completely randomly, i.e. start with a different seed value and generate the first random number between 1 to n to choose the first node, then a second random number between 1 to n-1 to choose the second node, and join the two nodes with an edge.

  <img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartA_1.png" width="500px">
  
 - The histogram is plotted between ratio of the same linking edge to all the edges and Frequency.
 - The red line denotes the value of the same ratio for the actual graph. The percentage corresponding to the line denotes the probability parameter.
 - The histogram is a typical bell shaped curve
 - The statistics of the actual graph lies to the right of the peak of the bell curve
 - The probability parameter obtained is really less indicating the probability of the ration lying outside of the range is as low as 0.1378%
 
<img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartA_2.png" width="500px">

 - The histogram is plotted between Clustering coefficient considering all the edges and Frequency.
 - The red line denotes the value of the same ratio for the actual graph. The percentage corresponding to the line denotes the probability parameter.
 - The probability parameter obtained is really less indicating the probability of the ration lying outside of the range is as low as 0.0205%
 
<img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartA_3.png" width="500px">
 
 - The histogram is plotted between Clustering coefficient considering same linking edges and Frequency.
 - The red line denotes the value of the same ratio for the actual graph. The percentage corresponding to the line denotes the probability parameter.
 - The probability parameter obtained is really less indicating the probability of the ration lying outside of the range is as low as 0.1171%
 
## Part B (Politics-related Blogs)

### Dataset

The dataset used in this visualization contains network patterns of politics- related blogs all over the internet. The blogs are classified as left leaning or right leaning in its political stance. Using this visualization we try and find out if people like to read diverse blogs that touch upon several different affiliations or they rather like to read stuff that possibly resonates with their own viewpoints. In the entire dataset, there are 1490 nodes each representing a particular blog, its name, source and affiliations.

### Visualization

<img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartB_5.png" height="300px">
<img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartB_6.png" height="300px">

 * The graph considers a cluster of blogs as nodes and the edges as people who have read the 2 blogs that the edge connects.
 * Each node represents a cluster of blogs. It contains a particular no. of nodes of both types.
 * In making these clusters of blogs, we are focussing on maximizing the density of the edges in each cluster.
 * Every node is colour coded. Each code is coloured according to the affiliations of the majority of blogs in it.
 * The graph contains clusters. The 2 opposite clusters representing the 2 opposing political views namely the capitalists and the democrats.
 
### K-means Clustering

K-means clustering is a method of cluster analysis which aims to partition n observations into k clusters. The algorithm we have implemented in this visualization uses the fundamental principles of k-means clustering along with the method of triads. The algorithm goes as follows. We take a weighted graph. Each edge has a weight attached to it.
 
Consider every node as an individual cluster. Take the most weighted edge and represent it, connecting the two nodes, combining them into the same cluster and thus reducing the no. of clusters by one Keep doing this till the clusters are reduced to a particular number; say k
                             
In this algorithm, the weight attached to each edge is equal to the no. of triads an edge is involved in, where, a triad represents a closed path from node A back to Node A using only 2 different nodes in between.

### Statistical Analysis

  <img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartB_1.png" height="200px">
  <img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartB_2.png" height="200px">
 
 - The histogram is plotted between ratio of the same linking edge to all the edges and Frequency.
 - The probability parameter obtained is really less indicating the probability of the ration lying outside of the range is as low as 0.0029% 
 - The final graph is not so detailed because it is taken in a wider range so the data is clubbed
 
<img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartB_3.png" height="200px">
<img src="https://raw.githubusercontent.com/nitika26/clustering-project/master/graphs/PartB_4.png" height="200px">

 - The histogram is plotted between Clustering coefficient considering all the edges and Frequency.
 - The probability parameter obtained is really less indicating the probability of the ration lying outside of the range is as low as 0.0029% 


  

