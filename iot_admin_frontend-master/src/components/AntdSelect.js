import { Select} from 'antd';
import React from 'react';

class AntdSelect extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            optionsLst: this.props.optionsLst
        }
    }

    onChange = (value) => {
        try{
            this.props.onValueChange(value);
        } catch (e) {

        }
    }
    
    onBlur = () => {
        // console.log('blur');
    }
    
    onFocus = () => {
        // console.log('focus');
    }
    
    onSearch = (val) => {
        console.log('search:', val);
    }

    render(){
        return <Select
            showSearch
            style={this.props.style}
            placeholder={this.props.placeholder}
            optionFilterProp="children"
            onChange={this.onChange}
            onFocus={this.onFocus}
            onBlur={this.onBlur}
            onSearch={this.onSearch}
            filterOption={(input, option) =>
                option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
            defaultValue={this.props.defaultValue}
            value={this.props.value}
        >
            {this.state.optionsLst.map(value => (
                <Select.Option value={value} key={value}>{value}</Select.Option>
            ))}
        </Select>;
    }

}


export default AntdSelect;