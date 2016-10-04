require 'socket'
require 'json'

s = TCPSocket.new '127.0.0.1', 2345

puts s.gets.chomp

#s.puts "COMMAND_RESPONSE CONNECTED"
s.puts "{\"command\": \"COMMAND_RESPONSE\",\"args\": [true]}"

puts JSON.parse s.gets.chomp
#s.puts "COMMAND_RESPONSE 1.0"
s.puts "{\"command\": \"COMMAND_RESPONSE\",\"args\": [1.0]}"

puts JSON.parse s.gets.chomp
#s.puts "COMMAND_RESPONSE Momentum"
s.puts "{\"command\": \"COMMAND_RESPONSE\",\"args\": [\"Momentum\"]}"

puts JSON.parse s.gets.chomp
#s.puts "COMMAND_RESPONSE TRUE"
s.puts "{\"command\": \"COMMAND_RESPONSE\",\"args\": [true]}"

puts JSON.parse s.gets.chomp
#s.puts "COMMAND_RESPONSE TRUE"
s.puts "{\"command\": \"COMMAND_RESPONSE\",\"args\": [true]}"

sleep 1
s.puts "COMMAND MOVE(5)"

while true
    puts JSON.parse s.gets.chomp
end

s.close             # close socket when done
