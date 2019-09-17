Problem
Let’s say we have this ecommerce website called AmazinKart. AmazinKart gets its
product details from a third party json API which contains the product prices and other
details. You as a developer of AmazinKart needs to create a module which will :
1. Retrieve the product details by calling the third party json API
url: https://api.jsonbin.io/b/5d31a1c4536bb970455172ca/latest
2. Convert the prices to INR if the price is not in INR using exchangeratesapi.io

url: https://api.exchangeratesapi.io/latest

3. Save the json file (named output.json) into any output folder.
But what’s the point of having an ecommerce site without any discounts right?!
So let's add some promotions.
Unsurprisingly, running promotion is a complex game and our geeky product
managers wants multiple sets of them to be deployed simultaneously in production but
will run only one of them at a time.
Promotion Set A:
1. There will be a 7% discount on the price if the origin of the products is Africa
2. There will be a discount of 4% if the product rating is 2 and 8 % if the product
rating is below 2.
3. There will be a flat discount of Rs 100 on the products in these categories:
“electronics”,”furnishing” for items costing Rs 500 and above.
Promotion Set B:
1. There will be a 12% discount on the prices if the inventory is more than 20.
2. There will be a discount of 7% if the product arrival status is new.
Common Rules:
1. There can only be one discount applied at a time for each Promotion Set.
2. You have to apply the discount which provides the highest discount to the
customer
3. There will be a default discount of 2% if the price of the item exceeds Rs 1000
and no other discount was applied.

Running the program:
To run Promotion Set A:
java yourProgramName promotionSetA
To run Promotion Set B:
java yourProgramName promotionSetB

Running the above program should retrieve the product details from the API, convert
the prices if required, run the discount rules and then create a json file with a json node called discount.



Steps To Run
1. Use linux machine, and cd into the amazing folder
2. Create folder /var/log/forbes/logs/ for logs
3. To setup, run : bin/setup
4. For program, run : bin/amazing
5. To run program with a particular promotion set run : ./bin/amazing promotionSetA.csv 
6. To disable special promotion run : ./bin/amazing promotionSetA.csv false
