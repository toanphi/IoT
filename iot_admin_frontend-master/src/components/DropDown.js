import { Menu, Dropdown } from 'antd';
import { DownOutlined } from '@ant-design/icons';

const menu = (
    <Menu>
        <Menu.Item>
            <a target="_blank" rel="noopener noreferrer" href="/">
                By day
            </a>
        </Menu.Item>
        <Menu.Item >
            <a target="_blank" rel="noopener noreferrer" href="/">
                By month
            </a>
        </Menu.Item>
        <Menu.Item>
            <a target="_blank" rel="noopener noreferrer" href="/">
                By year
            </a>
        </Menu.Item>
    </Menu>
);

const dropdown = () => {
    return <Dropdown overlay={menu}>
        <a href="/" style={{float: 'right', marginRight: '20px'}} className="ant-dropdown-link" onClick={e => e.preventDefault()}>
            Sort <DownOutlined />
        </a>
    </Dropdown>
}

export default dropdown;