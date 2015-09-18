library(lattice);

em1 <- read.table("Empirical_FP_1_NN.distribution")
em2 <- read.table("Empirical_FP_2_NN.distribution")
em3 <- read.table("Empirical_FP_3_NN.distribution")
em4 <- read.table("Empirical_FP_4_NN.distribution")
em5 <- read.table("Empirical_FP_5_NN.distribution")

data <- c(
  1, median(em1$V1),
  2, median(em2$V1),
  3, median(em3$V1),
  4, median(em4$V1),
  5, median(em5$V1)
)
mat <- as.data.frame(matrix(data, ncol=2, nrow=5, byrow = TRUE))

pdf("median_acc_empirical.pdf", height = 8.27, width = 11.69)
xyplot(V2 ~ V1, mat, main = "Median accuracy for empirical data",
       xlab = "k",
       ylab = "Error in meters",
       type="l",
       grid = TRUE,
       lwd = 4
)
dev.off()