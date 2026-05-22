export interface NavigationItem {
  id: string;
  title: string;
  type: 'item' | 'collapse' | 'group';
  translate?: string;
  icon?: string;
  hidden?: boolean;
  url?: string;
  classes?: string;
  exactMatch?: boolean;
  external?: boolean;
  target?: boolean;
  breadcrumbs?: boolean;
  children?: NavigationItem[];
}
export const NavigationItems: NavigationItem[] = [
  {
    id: 'navigation',
    title: 'Navigation',
    type: 'group',
    icon: 'icon-navigation',
    children: [
      {
    id: 'dashboard',
    title: 'Dashboard',
    type: 'item',
    url: '/dashboard',
    icon: 'feather icon-home',
    classes: 'nav-item'
  }

    ]
  },
  {
    id: 'ui-element',
    title: 'USUARIOS',
    type: 'group',
    icon: 'icon-ui',
    children: [
      {
        id: 'basic',
        title: 'Usuarios',
        type: 'collapse',
        icon: 'feather icon-user',
        children: [
          {
            id: 'usuarios',
            title: 'Usuarios',
            icon: 'feather icon-users',
            type: 'item',
            url: '/basic/usuarios'
          }
        ]
      }
    ]
  },
  {
    id: 'ui-element_clientes',
    title: 'CLIENTES',
    type: 'group',
    icon: 'icon-ui',
    children: [
      {
        id: 'basic',
        title: 'Clientes',
        type: 'collapse',
        icon: 'feather icon-users',
        children: [
          {
            id: 'cliente_envia',
            title: 'Cliente_Envia',
            type: 'item',
            icon: 'feather icon-user',
            url: '/basic/clientes'
          },
          {
            id: 'clientes_recibe',
            title: 'Cliente_Recibe',
            type: 'item',
            icon: 'feather icon-user',
            url: '/basic/clientes_recibe'
          }
        ]
      }
    ]
  },

 {
    id: 'facturas',
    title: 'facturacion',
    type: 'group',
    icon: 'feather icon-file-text',
    children: [
      {
        id: 'factura',
        title: 'Facturacion',
        type: 'collapse',
        icon: 'feather icon-file-text',
        children:[
          {
            id: 'factura_detalle',
            title: 'Factura',
            type: 'item',
            icon: 'feather icon-file-text',
            url: '/basic/facturacion'
          },
          {
            id: 'factura_lista',
            title: 'Lista Facturas',
            type: 'item',
            icon: 'feather icon-folder',
            url: '/basic/lista_facturas'
          }
        ]
      },
      
    ]
  }
];
