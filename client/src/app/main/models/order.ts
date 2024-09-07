export interface Order {
  firstName: string;
  lastName: string;
  companyName: string;
  address: {
    country: string;
    city: string;
    address: string;
    postalCode: string;
  }
  phoneNumber: string;
  email: string;
  product: {
    productId: number;
    quantity: number;
  }[]
}