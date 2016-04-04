using System;
using System.Collections.Generic;
using System.IO;

namespace SqlDataGenerator {
    using System.Linq;
    using static Util;

    public class Program {
        public static void Main(string[] args) {
            // 100 users
            var profiles = new List<Profile>();
            for(int i = 1; i <= 100; i++) {
                profiles.Add(new Profile(i));
            }

            // 200 friendships
            var friendships = new HashSet<Friend>();
            while(friendships.Count < 200) {
                var friend1 = profiles[rand.Next(profiles.Count)];
                var friend2 = profiles[rand.Next(profiles.Count)];
                if(friend1 != friend2) {
                    var frienship = new Friend(friend1, friend2);
                    friendships.Add(frienship);
                }
            }

            // 10 groups
            var groups = new List<Group>();
            for(int i = 1; i <= 10; i++) {
                groups.Add(new Group(i, 100));
            }

            // 50 members
            var members = new HashSet<Member>();
            while(members.Count < 50) {
                int group_id = rand.Next(groups.Count) + 1;
                int user_id = rand.Next(profiles.Count) + 1;
                members.Add(new Member(group_id, user_id));
            }

            // 300 messages
            var messages = new List<Message>();
            for(int i = 1; i <= 300; i++) {
                if(rand.Next(4) == 0) { // 25% chance to generate group wide message
                    messages.Add(new Message(i, profiles[rand.Next(profiles.Count)], groups[rand.Next(groups.Count)]));
                } else {
                    messages.Add(new Message(i, profiles[rand.Next(profiles.Count)], profiles[rand.Next(profiles.Count)]));
                }
            }

            using(StreamWriter file = new StreamWriter("test_data.sql")) {
                foreach(var p in profiles) {
                    var query = $"INSERT INTO Profiles VALUES({p.user_id}, {p.fname}, {p.lname}, {p.email}, TIMESTAMP '{p.dob.ToString(DateFormat)}', TIMESTAMP '{p.last_on.ToString(DateFormat)}');";
                    Console.WriteLine(query);
                    file.WriteLine(query);
                }
                Console.WriteLine();
                file.WriteLine();

                foreach(var f in friendships.OrderBy(f => f.friend1_id).ThenBy(f => f.friend2_id)) {
                    var query = $"INSERT INTO Friends VALUES({f.friend1_id}, {f.friend2_id}, {f.status}, TIMESTAMP '{f.established.ToString(DateFormat)}');";
                    Console.WriteLine(query);
                    file.WriteLine(query);
                }
                Console.WriteLine();
                file.WriteLine();

                foreach(var g in groups) {
                    var query = $"INSERT INTO Groups VALUES({g.group_id}, {g.name}, {g.description}, {g.capacity});";
                    Console.WriteLine(query);
                    file.WriteLine(query);
                }
                Console.WriteLine();
                file.WriteLine();

                foreach(var m in members.OrderBy(m => m.group_id).ThenBy(m => m.user_id)) {
                    var query = $"INSERT INTO Members VALUES({m.group_id}, {m.user_id});";
                    Console.WriteLine(query);
                    file.WriteLine(query);
                }
                Console.WriteLine();
                file.WriteLine();

                foreach(var m in messages) {
                    var query = $"INSERT INTO Messages VALUES({m.msg_id}, {m.subject}, {m.sender_id}, {m.recip_id}, TIMESTAMP '{m.time_sent.ToString(DateFormat)}', {m.msg_text}, {m.type});";
                    Console.WriteLine(query);
                    file.WriteLine(query);
                }
            }
        }
    }
}
