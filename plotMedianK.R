args <- commandArgs(trailingOnly = TRUE)
data <- read.table(args[1])
colnames(data) <- c("r.x", "r.y", "r.z", "e.x", "e.y", "e.z", "k", "run_number")

data$error <- sqrt(
  (data$r.x - data$e.x) * (data$r.x - data$e.x) +
    (data$r.y - data$e.y) * (data$r.y - data$e.y) +
    (data$r.z - data$e.z) * (data$r.z - data$e.z) )
print("Plotting median accuracy for 0 < k < 6")
d <- c(
  1, median(data[data$k == 1, "error"]),
  2, median(data[data$k == 2, "error"]),
  3, median(data[data$k == 3, "error"]),
  4, median(data[data$k == 4, "error"]),
  5, median(data[data$k == 5, "error"])
)
mat <- as.data.frame(matrix(d, ncol=2, nrow=5, byrow = TRUE))

pdf(paste(args[1], "_median_acc.pdf"), height = 8.27, width = 11.69)
plot(V2 ~ V1, mat,
       xlab = "k",
       ylab = "Error in meters",
       type="l"
)
garbage <- dev.off()