## Plots for the Sidewalk simulation
 #
 # Takes the output of Plots.java, csv files, and creates basic plots.
 # 
 # Date: April 9, 2016

setwd("/Users/alexdombrowski/Desktop/sidewalk")
coverage = read.csv(file = "Coverage_sidewalkLength_10.0_dropDiameter_1.0.csv",
		 header = TRUE)
numDropsDistr = read.csv(file = "NumDropDistr_sidewalkLength_10.0_dropDiameter_1.0.csv")

par(mfrow = c(2, 1))
plot(coverage$sidewalkLength.10.0_dropDiameter.1.0,
	ylab = "Coverage (Length of Sidewalk that's wet)",
	xlab = "Number of drops",
	main = "Coverage Progression",
	type = 'l',
	pch = 16)

den = density(numDropsDistr$sidewalkLength.10.0_dropDiameter.1.056)
plot(den,
	ylab = "Density",
	xlab = "Number of drops",
	main = "Distribution of number of drops to make sidewalk wet")
par(mfrow = c(1, 1))
