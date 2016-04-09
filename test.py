# import csv
# my_list = ["1", "2"]
# my_list.insert(0, "")
# with open('movie-night-user-profile.csv', mode='wb') as file_user_profile:
# 	writer = csv.writer(file_user_profile)
# 	writer.writerow(my_list)




# import csv

# def parse_movie_name(name):
# 	start = len("Rating [")
# 	end = len(name) - 1
# 	return name[start:end]

# # print parse_movie_name("Rating [Star Wars]")

# list_user_rate = []
# list_movie = []
# with open('movienight.csv', mode='rb') as file_movie:
# 	reader_movie = csv.reader(file_movie)
# 	header_movie = reader_movie.next()
# 	new_header = []
# 	for name in header_movie:
# 		new_header.append(parse_movie_name(name))
# 	list_movie = new_header[5:32]
# print list_movie


# webster = {
#     "Aardvark" : "A star of a popular children's cartoon show.",
#     "Baa" : "The sound a goat makes.",
#     "Carpet": "Goes on the floor.",
#     "Dab": "A small amount."
# }

# # Add your code below!
# for x in webster:
#     print webster[x]













# def get_feature_value(movie_name):
# 	pass

# with open('movienight.csv', 'rb') as file_movie:
# 	reader_movie = csv.reader(file_movie)
# 	title_movie = reader_movie.next()
	
# 	with open('movie-night-movies-genres.csv', mode='rb') as file_genre:
# 		reader_genre = csv.reader(file_genre)
# 		title_genre = reader_genre.next()
# 		list_genre = title_genre
# 		for i, v in enumerate(title_genre):

		
# 		movies_in_genre = []
# 		for row_in_genre in reader_genre:
# 				movies_in_genre.append(row_in_genre[0])

# 		for movie_in_movie in title_movie:
# 			movie_found = False
# 			for movie_in_genre in movies_in_genre:
# 				# print "%s => %s" % (movie_in_movie, movie_in_genre)
# 				if movie_in_genre in movie_in_movie:
# 					movie_found = True
# 					print "%s ==found== %s" % (movie_in_movie, movie_in_genre)

# 			if not movie_found:
# 				print "!!!!!!!! %s not found" % movie_in_movie
		







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
