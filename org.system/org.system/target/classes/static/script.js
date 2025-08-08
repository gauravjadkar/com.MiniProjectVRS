const Api_url='http://localhost:8083/api/VehicleRentalSystemapi/VehicleRentalSystem';
async function vehicle_listing() {
    const form = document.getElementById('Booking');
    const rent_date = document.getElementById('rentdate').value;
    const return_date = document.getElementById('returndate').value;
    const vehicle_type = document.getElementById('vehicle_type').value;
    
    const container = document.getElementById('vehicle-container');
    container.innerHTML = "";
    
    if (!rent_date || !return_date) {
        container.innerHTML = "<p style='color:red;'>Please select both rent and return dates.</p>";
        return;
    }
    
    const url = `http://localhost:8083/api/VehicleRentalSystem/available?rentdate=${encodeURIComponent(rent_date)}&returndate=${encodeURIComponent(return_date)}&vehicletype=${encodeURIComponent(vehicle_type)}`;
    
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        
        const responseText = await response.text();
        console.log('Raw response:', responseText);
        
        
        let cleanedResponse = responseText;
  
        cleanedResponse = cleanedResponse.replace(/(\}])\}+]/g, '$1');
        cleanedResponse = cleanedResponse.replace(/\}+$/g, '}');
        
        console.log('Cleaned response:', cleanedResponse);
        
        let vehicles;
        try {
            vehicles = JSON.parse(cleanedResponse);
        } catch (parseError) {
            console.error('JSON Parse Error:', parseError);
            console.error('Problematic JSON:', cleanedResponse);
            throw new Error('Invalid JSON response from server');
        }
        
        if (!Array.isArray(vehicles) || vehicles.length === 0) {
            container.innerHTML = "<p>No vehicles available for selected dates and type.</p>";
            return;
        }
        
        vehicles.forEach(vehicle => {
            // Validate vehicle object has required properties
            if (!vehicle.vehicle_model || !vehicle.vehicle_type || !vehicle.seats || !vehicle.price || !vehicle.vehicle_id) {
                console.warn('Invalid vehicle object:', vehicle);
                return;
            }
            
            container.innerHTML += `
                <div class="vehicle-box">
                    <div class="vehicle-name">
                        <p>${vehicle.vehicle_model}</p>
                    </div>
                    <div class="vehicle-details">
                        <div class="info-row">
                            <div><span class="info-label">Type:</span> ${vehicle.vehicle_type}</div>
                            <div><span class="info-label">Seats:</span> ${vehicle.seats}</div>
                            <div><span class="info-label">Price:</span> ${vehicle.price}</div>
                        </div>
                    </div>
                    <div class="button-container">
                        <button class="btn btn-book" onclick="vehicle_book(${vehicle.vehicle_id})">Book Now</button>
                        <button class="btn btn-watch" onclick="addToWatchLater(${vehicle.vehicle_id})">Watch Later</button>
                    </div>
                </div>
            `;
        });
        
    } catch (error) {
        console.error('Error fetching vehicles:', error);
        container.innerHTML = "<p style='color:red;'>Failed to fetch vehicles. Please try again.</p>";
    }
}

async function vehicle_book(id) {
  try {
    // Fetch vehicle data
    const response = await fetch(`http://localhost:8083/api/VehicleRentalSystem/vehicle/${id}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const vehicle = await response.json();

    // Get rent & return date values
    const rent_Date = document.getElementById('rentdate').value;
    const return_Date = document.getElementById('returndate').value;

    if (!rent_Date || !return_Date) {
      alert("Please select both rent and return dates.");
      return;
    }

    // Calculate total days and total price
    const start = new Date(rent_Date);
    const end = new Date(return_Date);
    const timeDiff = end.getTime() - start.getTime();
    const totalDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

    if (totalDays <= 0) {
      alert("Return date must be after rent date.");
      return;
    }

    const totalPrice = totalDays * vehicle.price;

    const rental = {
      rentDate: rent_Date,
      returnDate: return_Date,
      total_Days: totalDays,
      total_Price: totalPrice,
      vehicle: {
        vehicle_id: vehicle.vehicle_id
      },
      user: {
        cust_id: parseInt(localStorage.getItem("customer_id"))
      }
    };

    // Show confirmation popup
    showConfirmationPopup(rental);

  } catch (error) {
    console.error("Error during booking:", error);
    alert('Booking process failed!');
  }
}


function Vehicle_Create() {
  const container = document.getElementById("toggle-container");

  container.innerHTML = `
    <form class="regform" id="vehicleForm">
        <input type="text" placeholder="Vehicle Model" id="vehiclemodel" required>
        <input type="text" placeholder="Vehicle Type" id="vehicletype" required>
        <input type="text" placeholder="Registered Number (RC No)" id="rcno" required>
        <input type="number" placeholder="Vehicle Seats" id="seats" required>
        <input type="number" placeholder="Price per Day" id="priceperd" required>
        <input type="text" placeholder="Password" id="pass" required>
        <button type="submit">Submit</button>
    </form>
  `;

  const form = document.getElementById("vehicleForm");

  form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const veh = {
      vehicle_model: document.getElementById("vehiclemodel").value.trim(),
      vehicle_type: document.getElementById("vehicletype").value.trim(),
      rc_no: document.getElementById("rcno").value.trim(),
      price: parseFloat(document.getElementById("priceperd").value),
      seats: parseInt(document.getElementById("seats").value),
      password: document.getElementById("pass").value,
      vehicle_available: true
    };

    console.log("Sending vehicle JSON:", JSON.stringify(veh));

    try {
      const response = await fetch("http://localhost:8083/api/VehicleRentalSystem/register/vehicle", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(veh)
      });

      if (response.ok) {
        const registeredVeh = await response.json();  
        const vehicleId = registeredVeh.vehicle_id;   

        
        localStorage.setItem("vehicle_id", vehicleId);
        alert("Vehicle successfully registered!");

        form.reset();
        window.location.href = "rental.html";
         // Redirect to home or dashboard
      } else {
        const err = await response.text();
        console.error("Server error:", err);
        alert("Registration failed. Please check your inputs.");
      }
    } catch (error) {
      console.error("Network error:", error);
      alert("Could not connect to server.");
    }
  });
}

async function customer_create() {
  const container = document.getElementById("toggle-container");

  container.innerHTML = `
    <form class="regform" id="Customerform">
        <input type="text" placeholder="Enter Name" id="custname" required>
        <input type="text" placeholder="License No" id="licenseno" required>
        <input type="text" placeholder="Phone No" id="phone" required>
        <input type="text" placeholder="Aadhaar Card No" id="adhaar" required> 
        <input type="text" placeholder="Password" id="password" required>
        <button type="submit">Submit</button>
    </form>
  `;

  const form = document.getElementById("Customerform");

  form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const cust = {
      cust_name: document.getElementById("custname").value.trim(),
      license_no: document.getElementById("licenseno").value.trim(),
      phone_no: document.getElementById("phone").value.trim(),
      cust_adhaar: document.getElementById("adhaar").value.trim(),
      pass: document.getElementById("password").value.trim()
    };

    try {
      const response = await fetch("http://localhost:8083/api/VehicleRentalSystem/register/customer", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(cust)
      });

      if (response.ok) {
        const registeredCustomer = await response.json(); 
        const customerId = registeredCustomer.cust_id;    

        localStorage.setItem("customer_id", customerId); 
        alert("Successfully Registered!");

        form.reset();
        window.location.href = "index.html";
      } else {
        const error = await response.text();
        console.error("Server error:", error);
        alert(" Registration failed. Please enter valid input.");
      }
    } catch (error) {
      console.error(" Network error:", error);
      alert(" Could not connect to the server.");
    }
  });
}


async function login() {
  const container = document.getElementById("toggle-container");

  container.innerHTML = `
    <form class="regform" id="loginform">
      <input type="text" id="reg_id" placeholder="Enter Registered Id" required>
      <input type="password" id="pass" placeholder="Enter Password" required>
      <button type="submit">Submit</button>
    </form>
  `;

  const form = document.getElementById("loginform");

  form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const id = document.getElementById("reg_id").value;
    const pass = document.getElementById("pass").value;

    try {
      
      const veh_response = await fetch(`http://localhost:8083/api/VehicleRentalSystem/vehicle/${id}`);
      if (veh_response.ok) {
        const text = await veh_response.text();
        if (text) {
          const veh = JSON.parse(text);
          if (veh.password === pass) {
            alert("Vehicle login successful!");
            localStorage.setItem("vehicle_id",id);

            window.location.href = "rental.html";
            return;
          }
        }
      }

      
      const cust_response = await fetch(`http://localhost:8083/api/VehicleRentalSystem/customer/${id}`);
      if (cust_response.ok) {
        const text = await cust_response.text();
        if (text) {
          const cust = JSON.parse(text);
          if (cust.pass === pass) {
            alert("Customer login successful!");
            localStorage.setItem("customer_id",id);
            window.location.href = "index.html";
            return;
          }
        }
      }

      alert("Invalid credentials!");
    } catch (error) {
      console.error("Login failed:", error);
      alert("Error in connecting  server.");
    }
  });
}

async function addToWatchLater(vehicleId) {
  console.log("Function Invoked");
    const custId = localStorage.getItem("customer_id"); 

    if (!custId) {
        alert("Please login first.");
        return;
    }

    const watchLaterData = {
        cust_id: custId,
        vehicle_id: vehicleId
    };

    try {
        const response = await fetch("http://localhost:8083/api/VehicleRentalSystem/watchlater", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(watchLaterData)
        });

        if (response.ok) {
            alert("Vehicle added to Watch Later!");
        } else {
            alert("Failed to add to Watch Later.");
        }
    } catch (error) {
        console.error("Error adding to Watch Later:", error);
    }
}
