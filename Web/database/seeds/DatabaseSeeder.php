<?php

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        // $this->call(UsersTableSeeder::class);
        $this->call(PlacesTableSeeder::class);
        $this->call(AdditionalInfosTableSeeder::class);
        $this->call(WalletsTableSeeder::class);
        $this->call(LanguagesTableSeeder::class);
        $this->call(WorkTimesTableSeeder::class);
        $this->call(JobsTableSeeder::class);
    }
}
