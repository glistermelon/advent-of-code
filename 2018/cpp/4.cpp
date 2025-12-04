#include "DaySolver.hpp"

#include "utils.hpp"
#include "Array2D.hpp"

#include <map>

struct Time {

    int year, month, day, hour, minute;

    bool operator<(const Time& other) const {
        if (year != other.year) return year < other.year;
        if (month != other.month) return month < other.month;
        if (day != other.day) return day < other.day;
        if (hour != other.hour) return hour < other.hour;
        return minute < other.minute;
    }

    int duration_to(const Time& later) const {
        if (!(year == later.year && month == later.month && day == later.day)) {
            constexpr std::string_view err = "duration_to day mismatch";
            std::cout << err << std::endl;
            throw std::runtime_error(std::string(err));
        }
        if (!(hour == 0 && later.hour == 0)) {
            constexpr std::string_view err = "duration_to hours must be zero";
            std::cout << err << std::endl;
            throw std::runtime_error(std::string(err));
        }
        return later.minute - minute;
    }

};

struct Record {
    Time time;
    std::string str;
};

class Day4Solver : public DaySolver {

    // guard id : [wake up time, fall asleep time, wake up time, ...]]
    std::map<int, std::vector<Time>> schedules;

protected:

    void process_input() {

        std::vector<Record> records;

        std::istringstream input_stream(puzzle_input);
        std::string line;
        while (std::getline(input_stream, line, '\n')) {

            std::string time_str = line.substr(1, line.find(']') - 1);
            time_str[time_str.find(' ')] = '-';
            time_str[time_str.find(':')] = '-';
            std::vector<std::string> time_str_parts = split(time_str, '-');
            std::vector<int> time_parts = map_to<int>(time_str_parts, [](std::string s) { return std::stoi(s); });

            Time t;
            t.year = time_parts[0];
            t.month = time_parts[1];
            t.day = time_parts[2];
            t.hour = time_parts[3];
            t.minute = time_parts[4];

            line = line.substr(line.find(']') + 2);
            records.push_back({ t, line });

        }

        std::sort(
            records.begin(), records.end(),
            [](const Record& p1, const Record& p2) {
                return p1.time < p2.time;
            }
        );

        int guard;
        std::vector<Time>* schedule;
        for (const Record& record : records) {
            if (record.str.at(0) == 'G') {
                guard = std::stoi(split(record.str, ' ')[1].substr(1));
                schedule = &schedules[guard];
                if (schedule->size() % 2 == 0) {
                    schedule->push_back(record.time);
                }
            }
            else {
                schedule->push_back(record.time);
            }
        }

        // for (auto t : schedules[10]) {
        //     std::cout << t.year << ", " << t.month << ", "<< t.day << ", "<< t.hour << ", "<< t.minute << std::endl;
        // }

    }

    DaySolution solve_part_1() {

        int max_sleep_duration = 0;
        int max_sleep_duration_guard = -1;
        for (const auto& [guard, schedule] : schedules) {
            for (int i = 1; i < schedule.size(); i += 2) {
                const Time& fall_asleep = schedule[i];
                const Time& wake_up = schedule[i + 1];
                int duration = fall_asleep.duration_to(wake_up);
                if (duration > max_sleep_duration) {
                    max_sleep_duration = duration;
                    max_sleep_duration_guard = guard;
                }
            }
        }

        int minute_sleep_times[60] = { };
        const auto& schedule = schedules[max_sleep_duration_guard];
        for (int i = 1; i < schedule.size(); i += 2) {
            const Time& fall_asleep = schedule[i];
            const Time& wake_up = schedule[i + 1];
            for (int minute = fall_asleep.minute; minute < wake_up.minute; minute++) {
                minute_sleep_times[minute]++;
            }
        }

        int max_sleep_minute_count = 0;
        int max_sleep_minute = 0;
        for (int h = 0; h < 60; h++) {
            int count = minute_sleep_times[h];
            if (count > max_sleep_minute_count) {
                max_sleep_minute_count = count;
                max_sleep_minute = h;
            }
        }

        return max_sleep_minute * max_sleep_duration_guard;

    }

    DaySolution solve_part_2() {

        int max_guard_id = 0;
        for (const auto& [id, _] : schedules) {
            max_guard_id = std::max(max_guard_id, id);
        }
        Array2D<int> freq_map(max_guard_id + 1, 60);

        for (const auto& [guard, schedule] : schedules) {
            for (int i = 1; i < schedule.size(); i += 2) {
                const Time& fall_asleep = schedule[i];
                const Time& wake_up = schedule[i + 1];
                for (int minute = fall_asleep.minute; minute < wake_up.minute; minute++) {
                    freq_map[guard][minute]++;
                }
            }
        }

        int max_freq = 0, max_freq_guard = 0, max_freq_minute = 0;
        for (int guard = 0; guard <= max_guard_id; guard++) {
            for (int minute = 0; minute < 60; minute++) {
                auto freq = freq_map[guard][minute];
                if (freq > max_freq) {
                    max_freq = freq;
                    max_freq_guard = guard;
                    max_freq_minute = minute;
                }
            }
        }

        return max_freq_minute * max_freq_guard;

    }

};

DaySolver* DaySolver::get_current_day_solver() {
    return new Day4Solver();
}