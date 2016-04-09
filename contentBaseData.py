import csv

def parse_movie_name(name):
	start = len("Rating [")
	end = len(name) - 1
	return name[start:end]

# print parse_movie_name("Rating [Star Wars]")

list_user_rate = []
list_user_rate_mean = []
# list_movie = []
with open('movienight.csv', mode='rb') as file_movie:
	reader_movie = csv.reader(file_movie)
	header_movie = reader_movie.next()
	new_header = []
	for name in header_movie:
		new_header.append(parse_movie_name(name))
	# list_movie = new_header[5:32]

	# 3/1/2016 19:05:31,224,20-30,Male,"Comedy, Action, Sci-Fi & Fantasy, Animation, Drama",4,N/A,5,3,5,5,N/A,4,4,N/A,N/A,N/A,5,3,4,4,4,N/A,N/A,3,5,4,5,5,5,5,N/A,
	for row in reader_movie:
		user_rates = {}
		count = 0
		total_rate = 0.0
		for i in range(5, 32):
			if row[i] != "N/A":
				user_rates[new_header[i]] = int(row[i])
				count += 1
				total_rate += int(row[i])
		# print user_rates
		if count == 0:
			list_user_rate_mean.append(0)
		else:
			list_user_rate_mean.append(round(total_rate / count, 2))
		list_user_rate.append(user_rates)
# print list_user_rate_mean

dict_movie_genres = {}
list_genre = []

# parse 
with open('movie-night-movies-genres.csv', mode='rb') as file_genre:
	reader_genre = csv.reader(file_genre)
	title_genre = reader_genre.next()
	list_genre = title_genre[1:]

	# Star Wars,,,x,,,,
	for row in reader_genre:
		movie_name = row[0]
		genre_of_movie = []
		for i, v in enumerate(row):
			if v == 'x':
				genre_of_movie.append(title_genre[i])
		dict_movie_genres[movie_name] = genre_of_movie



list_user_profile = []
index = 0
for user_rates in list_user_rate:
	user_profile = []

	for genre in list_genre:
		rate_genre = 0.0
		count = 0
		for name in user_rates:
			# print user_rates[name]

			if name in dict_movie_genres and genre in dict_movie_genres[name]:
				rate_genre += user_rates[name]
				count += 1
		if count == 0:
			user_profile.append(0)
		elif round(rate_genre / count, 2) >= list_user_rate_mean[index]:
			user_profile.append(1)
		else:
			user_profile.append(0)

	list_user_profile.append(user_profile)
	index += 1

with open('movie-night-user-profile.csv', mode='wb') as file_user_profile:
	writer = csv.writer(file_user_profile)
	writer.writerow(list_genre)
	for user_profile in list_user_profile:
		writer.writerow(user_profile)







