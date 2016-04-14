import csv

dict_movie_genres = {}
list_genre = []

# parse 
with open('movie-night-movies-genres.csv', mode='rb') as file_genre:
	reader_genre = csv.reader(file_genre)
	header = reader_genre.next()
	with open('movie-night-content-base.csv', mode = 'wb') as file_content_base:
		writer_content_base = csv.writer(file_content_base)

		movie_id = 1
		genre_id = 1
		# Star Wars,,,x,,,,
		for row in reader_genre:
			for i, v in enumerate(row):
				if v == 'x':
					genre_id = i
					writer_content_base.writerow([movie_id, genre_id, 1])
			movie_id += 1

test_user_id = 139

with open('movie-night-user-profile.csv', mode = 'rb') as file_user_profile:
	reader_user_profile = csv.reader(file_user_profile)
	header = reader_user_profile.next()

	with open('movie-night-content-base.csv', mode = 'ab') as file_content_base:
		writer_content_base = csv.writer(file_content_base)

		user_id = 101

		# 0,1,0,1,0,1,0
		for row in reader_user_profile:
			if user_id == test_user_id:
				genre_id = 1
				for v in row:
					if int(v) > 0:
						writer_content_base.writerow([user_id, genre_id, v])
					genre_id += 1
				break
			user_id += 1




