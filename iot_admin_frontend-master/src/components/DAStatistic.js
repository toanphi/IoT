//import { Statistic, Card, Row, Col } from 'antd';
//import { ArrowUpOutlined, ArrowDownOutlined } from '@ant-design/icons';
//import React from 'react';
//
//class DAStatistic extends React.Component {
//    constructor(props){
//        super(props)
//
//        this.state = {
//
//        }
//    }
//
//    render(){
//        var value = this.props.value
//        var color = value < 0 ? '#cf1322' : '#3f8600'
//        var prefix = value < 0 ? <ArrowDownOutlined /> : <ArrowUpOutlined />
//        return <div className="site-statistic-demo-card">
//        <Row gutter={16}>
//            <Col span={8}>
//                <Card>
//                    <Statistic
//                        title={this.props.title}
//                        value={value}
//                        precision={2}
//                        valueStyle={{ color: color }}
//                        prefix={prefix}
//                        suffix="%"
//                    />
//                </Card>
//            </Col>
//            <Col span={8}>
//                <Card>
//                    <Statistic
//                        title="Number of records"
//                        value={this.props.noc}
//                        precision={0}
//                        valueStyle={{ color: '#cf1322' }}
//                    // prefix={<ArrowDownOutlined />}
//                    // suffix="%"
//                    />
//                </Card>
//            </Col>
//            <Col span={8}>
//                <Card>
//                    <Statistic
//                        title="Total records"
//                        value={this.props.totalRecord}
//                        precision={0}
//                        valueStyle={{ color: '#cf1322' }}
//                    // prefix={<ArrowDownOutlined />}
//                    // suffix="%"
//                    />
//                </Card>
//            </Col>
//        </Row>
//    </div>
//    }
//}
//
//
//export default DAStatistic;