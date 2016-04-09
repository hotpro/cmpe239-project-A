import csv

with open('movienight.csv', 'rb') as file_movie:
	reader_movie = csv.reader(file_movie)
	title_movie = reader_movie.next()
	
	with open('movie-night-movies-genres.csv', mode='rb') as file_genre:
		reader_genre = csv.reader(file_genre)
		title_genre = reader_genre.next()
		title_genre = reader_genre.next()

		movies_in_genre = []
		for row_in_genre in reader_genre:
				movies_in_genre.append(row_in_genre[0])
				

		for movie_in_movie in title_movie:
			movie_found = False
			for movie_in_genre in movies_in_genre:
				# print "%s => %s" % (movie_in_movie, movie_in_genre)
				if movie_in_genre in movie_in_movie:
					movie_found = True
					print "%s ==found== %s" % (movie_in_movie, movie_in_genre)

			if not movie_found:
				print "!!!!!!!! %s not found" % movie_in_movie








				# if movie_in_genre in movie_in_movie:
				# 	movie_found = True
				# 	print movie_in_movie, movie_in_genre

			# if not movie_found:
			# 	print "!!!!!!!! %s not found" % movie_in_movie
		# for movie_in_genre in reader_genre:
		# 	movie_found = False
		# 	for row_in_genre in title_movie:
		# 		movie_in_genre = row_in_genre[0]
		# 		if movie_in_genre.lower() in movie_in_movie.lower():
		# 			movie_found = True
		# 			print movie_in_movie, movie_in_genre

		# 	if not movie_found:
		# 		print "!!!!!!!! %s not found" % movie_in_genre


