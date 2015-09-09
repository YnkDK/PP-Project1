library(lattice);

args <- commandArgs(trailingOnly = TRUE)
cumDist <- read.table(args[1])

pdf(paste(args[1], ".pdf", sep = ""))
xyplot(V2 ~ V1, cumDist, main = "Cumulative Error Distribution",
    xlab = "Error in meters",
    ylab = "Relative frequency",
    type="l",
    grid = TRUE,
    xlim = c(0, max(cumDist$V1)),
    ylim = c(0, 1),
    lwd = 4
)
dev.off()