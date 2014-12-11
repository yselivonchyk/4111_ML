# import sys
import numpy as np
import random
import matplotlib.pyplot as plt
import seaborn as sns
from matplotlib.backends.backend_pdf import PdfPages

from matplotlib import rc

rc('text', usetex=True)
rc('font', family='serif')
plt.rcParams['text.latex.preamble']=[r'\usepackage{amsmath}']

def example_permutation(p, n, m, pos):
	nPerm = m
	nNeg = p + n - m

	eNeg = [False]*nNeg

	ePerm = [False]*(nPerm - p) + [True]*p
	random.shuffle(ePerm)

	if pos == 'first':
		return ePerm + eNeg

	elif pos == 'last':
		return eNeg + ePerm

	else:
		print 'WARNING: unknown position specifier given.'
		return eNeg + ePerm


def roc_curve(examples, p, n):
	dx = 1./n
	dy = 1./p

	N = n + p

	x = np.zeros(N)
	y = np.zeros(N)


	if examples[0] == False:
		x[0] = dx
	else:
		y[0] = dy


	for k in range(1, N):
		if examples[k] == False:
			x[k] = x[k-1] + dx
			y[k] = y[k-1]
		else:
			x[k] = x[k-1]
			y[k] = y[k-1] + dy

	return x, y

def auc_area(x, y):
	return np.trapz(y, x)

if __name__ == '__main__':

	p = 10
	n = 990

	f, (aAx, b1Ax, b2Ax) = plt.subplots(ncols=3)
	f.tight_layout()

	aColor = sns.color_palette("Set2", 10)
	bColor = sns.cubehelix_palette(7, start=1.5, rot=0.5, dark=0.1, light=.7, reverse=False)
	
	aAx.set_color_cycle(aColor)
	aAx.set_xlim([-0.003, 1.003])
	aAx.set_ylim([-0.003, 1.003])
	aAx.set_aspect('equal')
	aAx.set_title('(b)')
	aAx.set_xlabel('FP-Rate', fontsize=10)
	aAx.set_ylabel('TP-Rate', fontsize=10)

	b1Ax.set_color_cycle(bColor)
	b1Ax.set_xlim([-0.003, 1.003])
	b1Ax.set_ylim([-0.003, 1.003])
	b1Ax.set_aspect('equal')
	b1Ax.set_title('(c, i)')
	b1Ax.set_xlabel('FP-Rate', fontsize=10)
	b1Ax.set_ylabel('TP-Rate', fontsize=10)

	b2Ax.set_color_cycle(bColor)
	b2Ax.set_xlim([-0.003, 1.003])
	b2Ax.set_ylim([-0.003, 1.003])
	b2Ax.set_aspect('equal')
	b2Ax.set_title('(c, ii)')
	b2Ax.set_xlabel('FP-Rate', fontsize=10)
	b2Ax.set_ylabel('TP-Rate', fontsize=10)


	# Problem b)
	print 'Problem 4 (b)'

	for k in range(1, 10+1):
		examples = example_permutation(p, n, 1000, 'first')
		x, y = roc_curve(examples, p, n)
		print auc_area(x, y)

		aAx.plot(x, y)


	# Problem c)
	print '\nProblem 4 (c, i)'

	for m in [500, 100, 50, 25, 20, 15, 10]:
		examples = example_permutation(p, n, m, 'first')
		x, y = roc_curve(examples, p, n)
		print auc_area(x, y)

		b1Ax.plot(x, y, label='$m ='+str(m)+'$')

	print '\nProblem 4 (c, ii)'
	for m in [500, 100, 50, 25, 20, 15, 10]:
		examples = example_permutation(p, n, m, 'last')
		x, y = roc_curve(examples, p, n)
		print auc_area(x, y)

		b2Ax.plot(x, y, label='$m ='+str(m)+'$')

	b1Ax.legend(loc='upper right')
	b2Ax.legend(loc='upper left')

	# sns.set_style("ticks")
	# sns.despine(offset=5)
	f.set_size_inches(8.27, 11.69)

	pp = PdfPages('plot.pdf')
	plt.savefig(pp, format='pdf')
	pp.close()

	plt.show()

