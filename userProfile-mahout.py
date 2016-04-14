import csv

with open('movie-night-user-profile.csv', mode = 'rb') as file_user_profile:
	reader_user_profile = csv.reader(file_user_profile)
	header = reader_user_profile.next()

	with open('movie-night-user-genre-similarity.csv', mode = 'wb') as file_content_base:
		writer_content_base = csv.writer(file_content_base)

		user_id = 1

		# 0,1,0,1,0,1,0
		for row in reader_user_profile:
			genre_id = 1
			for v in row:
				if int(v) > 0:
					writer_content_base.writerow([user_id, genre_id, v])
				genre_id += 1
			user_id += 1



