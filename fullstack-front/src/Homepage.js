import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import './index.css';

export default function Menu() {
  const { id } = useParams(); // Get the ID from the URL (e.g., table number)
  const [cart, setCart] = useState([]);

  // Define menu items
  const menuItems = [
    { id: 1, name: 'Pizza', price: 10 },
    { id: 2, name: 'Burger', price: 7 },
    { id: 3, name: 'Pasta', price: 8 },
    { id: 4, name: 'Salad', price: 5 },
    { id: 5, name: 'Cola', price: 7 }
  ];

  const addToCart = (item) => {
    setCart([...cart, { id: item.id, name: item.name, price: item.price }]);
  };
  

  const submitOrder = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/order', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ cart, tableId: id }) // Send the table ID along with the cart
      });
      if (response.ok) {
        console.log("Order submitted successfully");
        setCart([]); // Clear the cart if desired
      } else {
        console.error("Failed to submit order");
      }
    } catch (error) {
      console.error("Error submitting order:", error);
    }
  };
  
  const getWaiter = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/waiter', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ tableId: id }), // Send table ID as part of the request
      });
      if (response.ok) {
        console.log('Waiter request sent successfully');
      } else {
        console.error('Failed to send waiter request');
      }
    } catch (error) {
      console.error('Error sending waiter request:', error);
    }
  };
  
  const getCheck = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/check', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ tableId: id }), // Send table ID as part of the request
      });
      if (response.ok) {
        console.log('Check request sent successfully');
      } else {
        console.error('Failed to send check request');
      }
    } catch (error) {
      console.error('Error sending check request:', error);
    }
  };
  

  return (
    <div className="menu">
      <h2>Menu for Table {id}</h2> {/* Display the table or route ID */}
      <div className="menu-grid">
        {menuItems.map((item) => (
          <div className="menu-item" key={item.id}>
            <span className="item-name">{item.name} - ${item.price}</span>
            <button className="add-to-cart" onClick={() => addToCart(item)}>
              Add to Cart
            </button>
          </div>
        ))}
      </div>

      <div className="button-container">
      <button className="get-waiter-button" onClick={getWaiter}>Get Waiter</button>
      <button className="submit-order-button" onClick={submitOrder}>Submit Order</button>
      <button className="get-check-button" onClick={getCheck}>Get Check</button>
      </div>

      <div className="cart">
        <h3>Cart</h3>
        {cart.map((order, index) => (
          <div key={index}>
           {order.name} - ${order.price}
          </div>
        ))}
      </div>
    </div>
  );
}
