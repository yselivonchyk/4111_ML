import numpy as np
import random
import matplotlib.pyplot as plt
from matplotlib2tikz import save as tikz_save


def generate_examples(n):

	# Array of function arguments.
	x = np.zeros(n)
	# Array of corresponding function values.
	y = np.zeros(n)

	for k in range(0, n):
		x[k] = random.random()
		noise = random.gauss(0, 0.2)
		y[k] = np.sin(2*np.pi*x[k]) + noise

	return x, y

def regression(x, y, Lambda):
	n = x.size
	X = np.array([np.ones(n), x, x**2, x**3, x**4, x**5, x**6])
	X = (np.matrix(X)).transpose()

	a = ((X.T*X + Lambda*np.eye(7)).I)*X.T*np.matrix(y).T
	return a

def evaluate_poly(x, a):
	n = x.size
	X = np.array([np.ones(n), x, x**2, x**3, x**4, x**5, x**6])
	X = (np.matrix(X)).transpose()
	y = X*a
	return y.A1

def l2norm(x, y):
	areaSquared = np.trapz(y**2, x)
	return (areaSquared**0.5)


if __name__ == '__main__':

	random.seed(4)

	# Generate examples.
	xExample, yExample = generate_examples(10)
	
	LambdaArray = [0, 1e-8, 1e-6, 1e-4, 1e-2, 1]
	errorArray = []
	for k in range(0, 6):

		Lambda = LambdaArray[k]

		# Do ridge regression.
		a = regression(xExample, yExample, Lambda)

		# Plot the examples
		plt.figure(k)
		plt.plot(xExample, yExample, marker='x', ls='', color='r', label='examples')

		# Plot the function that we want to approximate with ridge regression.
		x = np.linspace(0, 1)
		yTrue = np.sin(2*np.pi*x)
		plt.plot(x, yTrue, color='b', label='$\sin(2\pi x)$')

		# Plot approxiamted function.
		yLearned = evaluate_poly(x, a)
		plt.plot(x, yLearned, color='g', label='approximation')

		plt.ylim([-1.4, 1.4])
		plt.xlabel('$x$')
		plt.ylabel('$f(x)$')
		plt.title('$\lambda ='+str(Lambda)+'$')
		if k==5:
			plt.legend(['examples', '$\sin(2\pi x)$', 'approx.'])

		tikz_save('../Plots/plot'+ str(k) + '.tikz',
			figureheight = '0.4\\textwidth',
			figurewidth = '0.5\\textwidth'
			)

		# Calculate L2 norm.
		errorArray.append(l2norm(x, yTrue - yLearned))

	print 'L2 error norm:\n'
	print errorArray
	plt.show()


