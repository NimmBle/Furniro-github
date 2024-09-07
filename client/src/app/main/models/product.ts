export interface Product {
  id: string;
  name: string;
  short_description: string;
  description: string;
  discount: number;
  mark_as_new: boolean;
  price: number;
  quality: number;
  cover_photo: string;
  sizes:Array<string>;
  colors:Array<string>;
}