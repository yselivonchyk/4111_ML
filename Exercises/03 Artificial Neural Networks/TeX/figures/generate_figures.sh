#!/bin/bash
for f in *.tex
do
	latexmk $f
	latexmk -c $f
done

for f in *.dvi
do
	dvips $f
done

for f in *.ps
do
	ps2pdf $f
done

for f in *.pdf
do
	pdfcrop $f $f
done

rm *.ps
rm *.dvi
