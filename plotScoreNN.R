args <- commandArgs(trailingOnly = TRUE)
cumDist <- read.table(args[1])

pdf(paste(args[1], ".pdf", sep = ""))
plot(cumDist, main = "Cumulative Error Distribution",
    xlab = "Error in meters",
    ylab = "Relative frequency"
)
dev.off()