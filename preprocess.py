import csv

# open original file for read
with open('movienight.csv', 'rb') as f:
	reader = csv.reader(f)

	# create output file
	with open('movienight_2.csv', mode='wb') as outfile:
		writer = csv.writer(outfile)
		title = reader.next()
		count = 1;
        # iterater row by row, column by column, and write to output file
		for row in reader:
			for i in range(5, 32):
				# print count, title[i], row[i]
				if row[i] != "N/A":
					writer.writerow([count, i - 4, row[i]])
			count += 1