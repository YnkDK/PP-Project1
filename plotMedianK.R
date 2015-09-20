args <- commandArgs(trailingOnly = TRUE)
data <- read.table(args[1])
colnames(data) <- c("r.x", "r.y", "r.z", "e.x", "e.y", "e.z", "k", "run_number")

data$error <- sqrt(
  (data$r.x - data$e.x) * (data$r.x - data$e.x) +
    (data$r.y - data$e.y) * (data$r.y - data$e.y) +
    (data$r.z - data$e.z) * (data$r.z - data$e.z) )

d <- c(
  1, median(data[data$k == 1, "error"]),
  2, median(data[data$k == 2, "error"]),
  3, median(data[data$k == 3, "error"]),
  4, median(data[data$k == 4, "error"]),
  5, median(data[data$k == 5, "error"])
)
mat <- as.data.frame(matrix(d, ncol=2, nrow=5, byrow = TRUE))

pdf("median_acc_empirical.pdf", height = 8.27, width = 11.69)
plot(V2 ~ V1, mat, main = "Median accuracy for empirical data",
       xlab = "k",
       ylab = "Error in meters",
       type="l"
)
dev.off()