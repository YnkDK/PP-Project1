args <- commandArgs(trailingOnly = TRUE)
data <- read.table(args[1])
colnames(data) <- c("r.x", "r.y", "r.z", "e.x", "e.y", "e.z", "k", "run_number")

data$error <- sqrt(
  (data$r.x - data$e.x) * (data$r.x - data$e.x) +
    (data$r.y - data$e.y) * (data$r.y - data$e.y) +
    (data$r.z - data$e.z) * (data$r.z - data$e.z) )

print("Plotting cumulative error function for k = 1 and k = 3")

k1 <- data[data$k == 1,]
error <- k1$error
n <- sum(!is.na(error))
error.ordered <- sort(error)

pdf(paste(args[1], "_k1.pdf"), height = 8.27, width = 11.69)
plot(error.ordered, (1:n)/n,
     type = 's',
     ylim = c(0, 1),
     xlab = 'Error in meters',
     ylab = 'Relative frequency',
     axes = FALSE
)
med <- median(error)
q95 <- quantile(error, probs = c(0.95), names = FALSE)

legend(q95, 0.95, paste('95%: ', round(q95, digits = 3), 'meters'), bty = 'n')
abline(v = q95, h = 0.95)

legend(q95, 0.5, paste('median:', round(med, digits = 3), 'meters'), bty = 'n')
abline(v = med, h = 0.5)

axis(side = 1, at = seq(0, max(error), 1))
axis(side = 2, at = seq(0, 1, 0.1))

garbage <- dev.off()

k3 <- data[data$k == 3,]
error <- k3$error
n <- sum(!is.na(error))
error.ordered <- sort(error)

pdf(paste(args[1], "_k3.pdf"), height = 8.27, width = 11.69)
plot(error.ordered, (1:n)/n,
     type = 's',
     ylim = c(0, 1),
     xlab = 'Error in meters',
     ylab = 'Relative frequency',
     axes = FALSE
)
med <- median(error)
q95 <- quantile(error, probs = c(0.95), names = FALSE)

legend(q95, 0.95, paste('95%: ', round(q95, digits = 3), 'meters'), bty = 'n')
abline(v = q95, h = 0.95)

legend(q95, 0.5, paste('median:', round(med, digits = 3), 'meters'), bty = 'n')
abline(v = med, h = 0.5)

axis(side = 1, at = seq(0, max(error), 1))
axis(side = 2, at = seq(0, 1, 0.1))

garbage <- dev.off()